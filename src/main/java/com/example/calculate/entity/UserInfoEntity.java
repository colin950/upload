package com.example.calculate.entity;

import com.example.calculate.audit.AbstractCreateUpdateDateEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Entity
@Data
@Table(name = "user_base")
public class UserInfoEntity extends AbstractCreateUpdateDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, unique = true, name = "nickname")
    private String nickname;

}
