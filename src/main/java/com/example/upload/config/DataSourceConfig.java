package com.example.upload.config;

import jakarta.persistence.EntityManagerFactory;
import com.example.upload.base.code.DataSourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final ApplicationContext context;

    private final Environment env;

    public static final String MASTER_DATASOURCE = "masterDataSource";

    public static final String SLAVE_DATASOURCE = "slaveDataSource";

    // *.properties , *.yml 파일에 있는 property를 자바 클래스에 값을 가져와서(바인딩) 사용할 수 있게 해주는 어노테이션
    @Bean(MASTER_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("username"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }

    // *.properties , *.yml 파일에 있는 property를 자바 클래스에 값을 가져와서(바인딩) 사용할 수 있게 해주는 어노테이션
    @Bean(SLAVE_DATASOURCE)
    @ConfigurationProperties(prefix = "spring.datasource.slave")
    public DataSource slaveDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl(env.getProperty("url"));
        dataSource.setUsername(env.getProperty("username"));
        dataSource.setPassword(env.getProperty("password"));
        return dataSource;
    }

    @Bean
    public DataSource routingDataSource(
            // autowired로 자동 주입하게 될경우 같은 객체 2개가 존재하여 선택할수 없기 때문에 Qualifier를 통해 이름을 정해서 생성
            @Qualifier(value = MASTER_DATASOURCE) DataSource masterDataSource,
            @Qualifier(value = SLAVE_DATASOURCE) DataSource slaveDataSource) {
        // 동적 라우팅을 위해 사용
        AbstractRoutingDataSource routingDataSource =
                new AbstractRoutingDataSource() {
                    @Override
                    protected Object determineCurrentLookupKey() {
                        DataSourceType dataSourceType =
                                RoutingDataSourceManager.getCurrentDataSourceName();

                        if (TransactionSynchronizationManager.isActualTransactionActive()
                                && dataSourceType == null) {
                            if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) {
                                dataSourceType = DataSourceType.SLAVE;
                            } else {
                                dataSourceType = DataSourceType.MASTER;
                            }
                        }
                        RoutingDataSourceManager.removeCurrentDataSourceName();
                        return dataSourceType;
                    }
                };
        Map<Object, Object> targetDataSources = new HashMap<>();

        targetDataSources.put(DataSourceType.MASTER, masterDataSource);
        targetDataSources.put(DataSourceType.SLAVE, slaveDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        return routingDataSource;
    }

    /**
     * Spring에서는 트랜잭션에 진입하는 순간 설정된 Datasource의 커넥션을 가져온다.
     * Ehcache같은 Cache를 사용하는 경우 실제 Database에 접근하지 않지만 불필요한 커넥션을 점유
     * Hibernate의 영속성 컨텍스트 1차캐시(실제 Database에 접근하지 않음) 에도 불필요한 커넥션을 점유
     * 외부 서비스(http, etc …)에 접근해서 작업을 수행한 이후에 그 결과값을 Database에 Read/Write하는 경우 외부 서비스에 의존하는 시간만큼 불필요한 커넥션 점유
     * Multi Datasource 환경에서 트랜잭션에 진입한 이후 Datasource를 결정해야할때 이미 트랜잭션 진입시점에 Datasource가 결정되므로 분기가 불가능
      */
    @Bean
    public DataSource lazyRoutingDataSource(
            @Qualifier(value = "routingDataSource") DataSource routingDataSource) {
        return new LazyConnectionDataSourceProxy(routingDataSource);
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    // 우선 주입을 위해서 primary 사용
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier(value = "routingDataSource") DataSource lazyRoutingDataSource) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(lazyRoutingDataSource);
        entityManagerFactoryBean.setPackagesToScan("com.example.upload.entity");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
        entityManagerFactoryBean.setJpaProperties(hibernateProperties());

        // springframework에 주입된 빈 리스트 조회
        ConfigurableListableBeanFactory beanFactory =
                ((ConfigurableApplicationContext) context).getBeanFactory();
        entityManagerFactoryBean
                .getJpaPropertyMap()
                // Select a custom batcher. 사용자 커스텀 배치
                .put(AvailableSettings.BATCH_STRATEGY, new SpringBeanContainer(beanFactory));
        return entityManagerFactoryBean;
    }
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put(
                "hibernate.show_sql", env.getProperty("spring.jpa.properties.hibernate.show_sql"));
        properties.put(
                "hibernate.format_sql",
                env.getProperty("spring.jpa.properties.hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto",
                env.getProperty("spring.jpa.properties.hibernate.ddl-auto"));
        properties.put(
                "hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put(
                "hibernate.implicit_naming_strategy",
                env.getProperty("spring.jpa.properties.hibernate.naming.implicit_strategy"));
        properties.put(
                "hibernate.physical_naming_strategy",
                "com.example.upload.entity.strategy.PhysicalNamingStrategy");
        properties.put(
                "hibernate.enable_lazy_load_no_trans",
                env.getProperty("spring.jpa.properties.hibernate.enable_lazy_load_no_trans"));
        return properties;
    }


}
