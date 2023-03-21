package com.example.search.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSearchCntBaseEntity is a Querydsl query type for SearchCntBaseEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSearchCntBaseEntity extends EntityPathBase<SearchCntBaseEntity> {

    private static final long serialVersionUID = -957471425L;

    public static final QSearchCntBaseEntity searchCntBaseEntity = new QSearchCntBaseEntity("searchCntBaseEntity");

    public final com.example.search.audit.QAbstractCreateUpdateDateEntity _super = new com.example.search.audit.QAbstractCreateUpdateDateEntity(this);

    public final NumberPath<Long> cnt = createNumber("cnt", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSearchCntBaseEntity(String variable) {
        super(SearchCntBaseEntity.class, forVariable(variable));
    }

    public QSearchCntBaseEntity(Path<? extends SearchCntBaseEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSearchCntBaseEntity(PathMetadata metadata) {
        super(SearchCntBaseEntity.class, metadata);
    }

}

