package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.OrderEntity;
import com.alaguna.orderkata.repository.UnprocessedOrderEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

class OrdersMother {

    static ResponseOrders getResponseOrders() {
        ResponseOrders responseOrders = new ResponseOrders();
        responseOrders.setPage("1");
        responseOrders.setContent(List.of(getOrderDTO()));
        return responseOrders;
    }

    static OrderDTO getOrderDTO() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUuid("123e4567-e89b-12d3-a456-426614174000");
        orderDTO.setId("1");
        orderDTO.setPriority("High");
        orderDTO.setDate("7/28/2012");
        orderDTO.setRegion("North America");
        orderDTO.setCountry("USA");
        orderDTO.setItem_type("Electronics");
        orderDTO.setSales_channel("Online");
        orderDTO.setShip_date("7/28/2015");
        orderDTO.setUnits_sold("100");
        orderDTO.setUnit_price("299.99");
        orderDTO.setUnit_cost("199.99");
        orderDTO.setTotal_revenue("29999.00");
        orderDTO.setTotal_cost("19999.00");
        orderDTO.setTotal_profit("10000.00");
        return orderDTO;
    }


    static UnprocessedOrderEntity getUnprocessOrder() {
        UnprocessedOrderEntity unprocessedOrderEntity = new UnprocessedOrderEntity();
        unprocessedOrderEntity.setUuid("123e4567-e89b-12d3-a456-426614174000");
        unprocessedOrderEntity.setId("1");
        unprocessedOrderEntity.setPriority("High");
        unprocessedOrderEntity.setDate("7/28/2012");
        unprocessedOrderEntity.setRegion("North America");
        unprocessedOrderEntity.setCountry("USA");
        unprocessedOrderEntity.setItemType("Electronics");
        unprocessedOrderEntity.setSalesChannel("Online");
        unprocessedOrderEntity.setShipDate("7/28/2015");
        unprocessedOrderEntity.setUnitsSold("100");
        unprocessedOrderEntity.setUnitPrice("299.99");
        unprocessedOrderEntity.setUnitCost("199.99");
        unprocessedOrderEntity.setTotalRevenue("29999.00");
        unprocessedOrderEntity.setTotalCost("19999.00");
        unprocessedOrderEntity.setTotalProfit("10000.00");
        return unprocessedOrderEntity;
    }

    static OrderEntity getOrder() {
        OrderEntity unprocessedOrderEntity = new OrderEntity();
        unprocessedOrderEntity.setUuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        unprocessedOrderEntity.setId("1");
        unprocessedOrderEntity.setPriority("High");
        unprocessedOrderEntity.setDate(LocalDate.of(2012,7,28));
        unprocessedOrderEntity.setRegion("North America");
        unprocessedOrderEntity.setCountry("USA");
        unprocessedOrderEntity.setItemType("Electronics");
        unprocessedOrderEntity.setSalesChannel("Online");
        unprocessedOrderEntity.setShipDate(LocalDate.of(2015,7,28));
        unprocessedOrderEntity.setUnitsSold(100);
        unprocessedOrderEntity.setUnitPrice(BigDecimal.valueOf(299.99));
        unprocessedOrderEntity.setUnitCost(BigDecimal.valueOf(199.99));
        unprocessedOrderEntity.setTotalRevenue(BigDecimal.valueOf(29999.00));
        unprocessedOrderEntity.setTotalCost(BigDecimal.valueOf(19999.00));
        unprocessedOrderEntity.setTotalProfit(BigDecimal.valueOf(10000.00));
        return unprocessedOrderEntity;
    }
}