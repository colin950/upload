package com.example.calculate.entity;

import com.example.calculate.audit.AbstractCreateUpdateDateEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Accessors(chain = true)
@Entity
@Data
@Table(name = "calculate_info")
public class CalculateInfoEntity extends AbstractCreateUpdateDateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private Long userId;

    private String currency;
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "x_room_id")
    private String xRoomId;

    @Column(nullable = false)
    private String status;

    @Column(name = "notify_setup_at")
    @JsonIgnore
    private LocalDateTime notifySetupAt;



}