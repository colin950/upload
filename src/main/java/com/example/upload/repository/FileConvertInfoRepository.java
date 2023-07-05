package com.example.upload.repository;

import com.example.upload.entity.FileConvertInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileConvertInfoRepository extends JpaRepository<FileConvertInfoEntity, Long> {
}
