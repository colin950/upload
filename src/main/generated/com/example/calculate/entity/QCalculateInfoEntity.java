package com.example.calculate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCalculateInfoEntity is a Querydsl query type for CalculateInfoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCalculateInfoEntity extends EntityPathBase<CalculateInfoEntity> {

    private static final long serialVersionUID = 163781733L;

    public static final QCalculateInfoEntity calculateInfoEntity = new QCalculateInfoEntity("calculateInfoEntity");

    public final com.example.calculate.audit.QAbstractCreateUpdateDateEntity _super = new com.example.calculate.audit.QAbstractCreateUpdateDateEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath currency = createString("currency");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> notifySetupAt = createDateTime("notifySetupAt", java.time.LocalDateTime.class);

    public final StringPath status = createString("status");

    public final NumberPath<java.math.BigDecimal> totalAmount = createNumber("totalAmount", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath xRoomId = createString("xRoomId");

    public QCalculateInfoEntity(String variable) {
        super(CalculateInfoEntity.class, forVariable(variable));
    }

    public QCalculateInfoEntity(Path<? extends CalculateInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCalculateInfoEntity(PathMetadata metadata) {
        super(CalculateInfoEntity.class, metadata);
    }

}

