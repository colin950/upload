package com.example.calculate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWalletEntity is a Querydsl query type for WalletEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWalletEntity extends EntityPathBase<WalletEntity> {

    private static final long serialVersionUID = -256232754L;

    public static final QWalletEntity walletEntity = new QWalletEntity("walletEntity");

    public final com.example.calculate.audit.QAbstractCreateUpdateDateEntity _super = new com.example.calculate.audit.QAbstractCreateUpdateDateEntity(this);

    public final NumberPath<java.math.BigDecimal> balance = createNumber("balance", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<java.math.BigDecimal> transferInUse = createNumber("transferInUse", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QWalletEntity(String variable) {
        super(WalletEntity.class, forVariable(variable));
    }

    public QWalletEntity(Path<? extends WalletEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWalletEntity(PathMetadata metadata) {
        super(WalletEntity.class, metadata);
    }

}

