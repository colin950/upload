package com.example.search.repository;

import com.example.search.entity.SearchCntBaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SearchCntBaseRepository extends JpaRepository<SearchCntBaseEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "update search_cnt_base scb " +
            "set scb.cnt = scb.cnt + 1 " +
            "where scb.keyword =:keyword ", nativeQuery = true)
    int updateSearchCnt(@Param("keyword") String keyword);

    @Query(value = "select sgb.* from search_cnt_base sgb " +
            "order by cnt desc limit :limit ", nativeQuery = true)
    List<SearchCntBaseEntity> findByKeyword(@Param("limit") int limit);
}
