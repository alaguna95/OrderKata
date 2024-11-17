package com.alaguna.orderkata.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "unprocessed_orders")
@Data
public class UnprocessedOrderEntity {

    @Id
    @GeneratedValue
    private int idRandom;

    private boolean processed;

    private String messageError;

    private String id;

    private String uuid;

    private String region;

    private String country;

    private String itemType;

    private String salesChannel;

    private String priority;

    private String date;

    private String shipDate;

    private String unitsSold;

    private String unitPrice;

    private String unitCost;

    private String totalRevenue;

    private String totalCost;

    private String totalProfit;

}