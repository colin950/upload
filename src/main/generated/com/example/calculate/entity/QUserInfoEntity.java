package com.example.calculate.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserInfoEntity is a Querydsl query type for UserInfoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserInfoEntity extends EntityPathBase<UserInfoEntity> {

    private static final long serialVersionUID = 189845870L;

    public static final QUserInfoEntity userInfoEntity = new QUserInfoEntity("userInfoEntity");

    public final com.example.calculate.audit.QAbstractCreateUpdateDateEntity _super = new com.example.calculate.audit.QAbstractCreateUpdateDateEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QUserInfoEntity(String variable) {
        super(UserInfoEntity.class, forVariable(variable));
    }

    public QUserInfoEntity(Path<? extends UserInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserInfoEntity(PathMetadata metadata) {
        super(UserInfoEntity.class, metadata);
    }

}

