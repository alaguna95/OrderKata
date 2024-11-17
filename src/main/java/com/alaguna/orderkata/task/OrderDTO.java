package com.alaguna.orderkata.task;

import lombok.Data;

@Data
class OrderDTO {

    private String uuid;

    private String id;

    private String priority;

    private String date;

    private String region;

    private String country;

    private String item_type;

    private String sales_channel;

    private String ship_date;

    private String units_sold;

    private String unit_price;

    private String unit_cost;

    private String total_revenue;

    private String total_cost;

    private String total_profit;

}
