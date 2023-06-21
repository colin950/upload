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
//@Table(name = "transfer_hist")
@Table(
        name = "transfer_hist",
        uniqueConstraints = {
                @UniqueConstraint(
                        // 한가지의 금액 전송에 따른 중복 처리 방지를 위해 unique
                        columnNames = {"hist_id", "hist_type"}
                )
        })
public class TransferHistEntity extends AbstractCreateUpdateDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Comment("전송 관련 index")
    @Column(name = "hist_id", nullable = false)
    private Long histId;

    @Comment("전송 이동 타입")
    @Column(name = "hist_type", nullable = false)
    private String histType;


    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    @Column(name = "receiver_user_id", nullable = false)
    private Long receiverUserId;
}
