package com.example.calculate.entity;

import com.example.calculate.audit.AbstractCreateUpdateDateEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Comment;

import java.io.Serializable;
import java.math.BigDecimal;

@Accessors(chain = true)
@Entity
@Data
@Table(name = "wallet")
public class WalletEntity extends AbstractCreateUpdateDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private BigDecimal balance;

    @Comment("자산 이동 중인 금액")
    private BigDecimal transferInUse;
}
