package com.alaguna.orderkata.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table(name = "orders")
@Entity
public class OrderEntity {

    @Id
    private UUID uuid;

    private String id;

    private String region;

    private String country;

    private String itemType;

    private String salesChannel;

    private String priority;

    private LocalDate date;

    private LocalDate shipDate;

    private Integer unitsSold;

    private BigDecimal unitPrice;

    private BigDecimal unitCost;

    private BigDecimal totalRevenue;

    private BigDecimal totalCost;

    private BigDecimal totalProfit;

}