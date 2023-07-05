package com.example.upload.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileConvertInfoEntity is a Querydsl query type for FileConvertInfoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileConvertInfoEntity extends EntityPathBase<FileConvertInfoEntity> {

    private static final long serialVersionUID = -681203815L;

    public static final QFileConvertInfoEntity fileConvertInfoEntity = new QFileConvertInfoEntity("fileConvertInfoEntity");

    public final com.example.upload.audit.QAbstractCreateUpdateDateEntity _super = new com.example.upload.audit.QAbstractCreateUpdateDateEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> fileInfoId = createNumber("fileInfoId", Long.class);

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath videoUrl = createString("videoUrl");

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QFileConvertInfoEntity(String variable) {
        super(FileConvertInfoEntity.class, forVariable(variable));
    }

    public QFileConvertInfoEntity(Path<? extends FileConvertInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileConvertInfoEntity(PathMetadata metadata) {
        super(FileConvertInfoEntity.class, metadata);
    }

}

