package com.example.upload.entity;

import com.example.upload.audit.AbstractCreateUpdateDateEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Entity
@Data
@Table(name = "file_info")
public class FileInfoEntity extends AbstractCreateUpdateDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "thumb_url")
    private String thumbUrl;

    @Column(name = "file_size")
    private Long fileSize;

    private int width;

    private int height;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    private String status;

    @Column(name = "convert_at")
    private LocalDateTime completedAt;
}
