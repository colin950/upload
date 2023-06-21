package com.example.calculate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCalculateDetailEntity is a Querydsl query type for CalculateDetailEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCalculateDetailEntity extends EntityPathBase<CalculateDetailEntity> {

    private static final long serialVersionUID = 1696697544L;

    public static final QCalculateDetailEntity calculateDetailEntity = new QCalculateDetailEntity("calculateDetailEntity");

    public final com.example.calculate.audit.QAbstractCreateUpdateDateEntity _super = new com.example.calculate.audit.QAbstractCreateUpdateDateEntity(this);

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    public final NumberPath<Long> calculateInfoId = createNumber("calculateInfoId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> notifySendAt = createDateTime("notifySendAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> senderUserId = createNumber("senderUserId", Long.class);

    public final StringPath status = createString("status");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCalculateDetailEntity(String variable) {
        super(CalculateDetailEntity.class, forVariable(variable));
    }

    public QCalculateDetailEntity(Path<? extends CalculateDetailEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCalculateDetailEntity(PathMetadata metadata) {
        super(CalculateDetailEntity.class, metadata);
    }

}

