package com.example.calculate.repository;

import com.example.calculate.entity.CalculateDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalculateDetailRepository extends JpaRepository<CalculateDetailEntity, Long> {
    Optional<CalculateDetailEntity> findByIdAndStatus(Long id, String status);

    List<CalculateDetailEntity> findAllByCalculateInfoIdAndStatus(Long calculateInfoId, String status);

    List<CalculateDetailEntity> findAllBySenderUserId(Long userId);
}
