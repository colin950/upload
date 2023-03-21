package com.example.search.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.search.audit.AbstractCreateUpdateDateEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;

import java.io.Serializable;

@Accessors(chain = true)
@Entity
@Data
@DynamicInsert
@Table(name = "search_cnt_base")
public class SearchCntBaseEntity extends AbstractCreateUpdateDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true, name = "keyword")
    private String keyword;

    @Column(name = "cnt")
    private Long cnt;

}
