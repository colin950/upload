package com.example.search.audit;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAbstractCreateUpdateDateEntity is a Querydsl query type for AbstractCreateUpdateDateEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QAbstractCreateUpdateDateEntity extends EntityPathBase<AbstractCreateUpdateDateEntity> {

    private static final long serialVersionUID = 766608736L;

    public static final QAbstractCreateUpdateDateEntity abstractCreateUpdateDateEntity = new QAbstractCreateUpdateDateEntity("abstractCreateUpdateDateEntity");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAbstractCreateUpdateDateEntity(String variable) {
        super(AbstractCreateUpdateDateEntity.class, forVariable(variable));
    }

    public QAbstractCreateUpdateDateEntity(Path<? extends AbstractCreateUpdateDateEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAbstractCreateUpdateDateEntity(PathMetadata metadata) {
        super(AbstractCreateUpdateDateEntity.class, metadata);
    }

}

