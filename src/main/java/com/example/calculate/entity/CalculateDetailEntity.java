package com.example.calculate.entity;

import com.example.calculate.audit.AbstractCreateUpdateDateEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Accessors(chain = true)
@Entity
@Data
//@Table(name = "calculate_detail")
@Table(
        name = "calculate_detail",
        uniqueConstraints = {
                @UniqueConstraint(
                        // 정산 요청 한건당 한번만 작성 되도록 unique
                        columnNames = {"calculate_info_id", "sender_user_id"}
                )
        })
public class CalculateDetailEntity extends AbstractCreateUpdateDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "calculate_info_id")
    private Long calculateInfoId;

    @Column(name = "sender_user_id", nullable = false)
    private Long senderUserId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;

    @Column(name = "notify_send_at")
    @JsonIgnore
    private LocalDateTime notifySendAt;
}
