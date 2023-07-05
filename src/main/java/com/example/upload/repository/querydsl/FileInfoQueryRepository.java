package com.example.upload.repository.querydsl;

import com.example.upload.base.dto.response.GetVideoInfoResDto;
import com.example.upload.base.dto.response.VideoInfoDetailResDto;
import com.example.upload.entity.QFileConvertInfoEntity;
import com.example.upload.entity.QFileInfoEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileInfoQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public GetVideoInfoResDto findByIdWithFileConvertInfo(Long id) {
        QFileInfoEntity qFileInfoEntity = QFileInfoEntity.fileInfoEntity;
        QFileConvertInfoEntity qFileConvertInfoEntity = QFileConvertInfoEntity.fileConvertInfoEntity;
        return jpaQueryFactory.from(qFileInfoEntity)
                .select(Projections.constructor(GetVideoInfoResDto.class,
                        qFileInfoEntity.id.as("id"),
                        qFileInfoEntity.title,
                        Projections.constructor(VideoInfoDetailResDto.class,
                                qFileInfoEntity.fileSize.as("originFileSize"),
                                qFileInfoEntity.width.as("originWidth"),
                                qFileInfoEntity.height.as("originHeight"),
                                qFileInfoEntity.videoUrl.as("originVideoUrl")
                                ),
                        Projections.constructor(VideoInfoDetailResDto.class,
                                qFileConvertInfoEntity.fileSize.as("fileSize"),
                                qFileConvertInfoEntity.width.as("width"),
                                qFileConvertInfoEntity.height.as("height"),
                                qFileConvertInfoEntity.videoUrl.as("videoUrl")
                                ),
                        qFileInfoEntity.createdAt
                        )
                )
                .where(qFileInfoEntity.id.eq(id))
                .leftJoin(qFileConvertInfoEntity)
                .on(
                        qFileInfoEntity.id.eq(qFileConvertInfoEntity.fileInfoId)
                ).fetchOne();
    }
}
