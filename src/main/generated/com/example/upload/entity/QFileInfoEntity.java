package com.example.upload.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFileInfoEntity is a Querydsl query type for FileInfoEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFileInfoEntity extends EntityPathBase<FileInfoEntity> {

    private static final long serialVersionUID = -85432196L;

    public static final QFileInfoEntity fileInfoEntity = new QFileInfoEntity("fileInfoEntity");

    public final com.example.upload.audit.QAbstractCreateUpdateDateEntity _super = new com.example.upload.audit.QAbstractCreateUpdateDateEntity(this);

    public final DateTimePath<java.time.LocalDateTime> completedAt = createDateTime("completedAt", java.time.LocalDateTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> fileSize = createNumber("fileSize", Long.class);

    public final NumberPath<Integer> height = createNumber("height", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath status = createString("status");

    public final StringPath thumbUrl = createString("thumbUrl");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath videoUrl = createString("videoUrl");

    public final NumberPath<Integer> width = createNumber("width", Integer.class);

    public QFileInfoEntity(String variable) {
        super(FileInfoEntity.class, forVariable(variable));
    }

    public QFileInfoEntity(Path<? extends FileInfoEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFileInfoEntity(PathMetadata metadata) {
        super(FileInfoEntity.class, metadata);
    }

}

