package com.example.calculate.repository;

import com.example.calculate.entity.CalculateInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalculateInfoRepository extends JpaRepository<CalculateInfoEntity, Long> {
}
