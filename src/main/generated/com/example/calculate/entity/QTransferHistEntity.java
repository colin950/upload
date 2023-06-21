package com.example.calculate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTransferHistEntity is a Querydsl query type for TransferHistEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransferHistEntity extends EntityPathBase<TransferHistEntity> {

    private static final long serialVersionUID = 1037430786L;

    public static final QTransferHistEntity transferHistEntity = new QTransferHistEntity("transferHistEntity");

    public final com.example.calculate.audit.QAbstractCreateUpdateDateEntity _super = new com.example.calculate.audit.QAbstractCreateUpdateDateEntity(this);

    public final NumberPath<java.math.BigDecimal> amount = createNumber("amount", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> histId = createNumber("histId", Long.class);

    public final StringPath histType = createString("histType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> receiverUserId = createNumber("receiverUserId", Long.class);

    public final NumberPath<Long> senderUserId = createNumber("senderUserId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QTransferHistEntity(String variable) {
        super(TransferHistEntity.class, forVariable(variable));
    }

    public QTransferHistEntity(Path<? extends TransferHistEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTransferHistEntity(PathMetadata metadata) {
        super(TransferHistEntity.class, metadata);
    }

}

