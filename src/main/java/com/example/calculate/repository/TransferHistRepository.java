package com.example.calculate.repository;

import com.example.calculate.entity.TransferHistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferHistRepository extends JpaRepository<TransferHistEntity, Long> {
}
