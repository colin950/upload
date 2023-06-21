package com.example.calculate.config;

import com.example.calculate.base.code.DataSourceType;

public class RoutingDataSourceManager {

    // 한 쓰레드에서 실행되는 코드가 동일한 객체를 사용할 수 있도록 해 주기 때문에 쓰레드와 관련된 코드에서
    // 파라미터를 사용하지 않고 객체를 전파하기 위한 용도로 주로 사용
    // 트랜잭션 컨텍스트 전파 - 트랜잭션 매니저는 트랜잭션 컨텍스트를 전파하는 데 ThreadLocal을 사용한다.
    // 사용자 인증정보 전파 - Spring Security에서는 ThreadLocal을 이용해서 사용자 인증 정보를 전파한다.
    private static final ThreadLocal<DataSourceType> currentDataSourceName = new ThreadLocal<>();

    public static void setCurrentDataSourceName(DataSourceType dataSourceType) {
        currentDataSourceName.set(dataSourceType);
    }

    public static DataSourceType getCurrentDataSourceName() { return currentDataSourceName.get(); }

    public static void removeCurrentDataSourceName() { currentDataSourceName.remove(); }
}
