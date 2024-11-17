package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.OrderEntity;
import com.alaguna.orderkata.repository.UnprocessedOrderEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

class OrderMapper {

    static UnprocessedOrderEntity fromDTOToUnprocessed(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }

        UnprocessedOrderEntity unprocessedOrderEntity = new UnprocessedOrderEntity();
        unprocessedOrderEntity.setId(orderDTO.getId());
        unprocessedOrderEntity.setUuid(orderDTO.getUuid());
        unprocessedOrderEntity.setRegion(orderDTO.getRegion());
        unprocessedOrderEntity.setCountry(orderDTO.getCountry());
        unprocessedOrderEntity.setItemType(orderDTO.getItem_type());
        unprocessedOrderEntity.setSalesChannel(orderDTO.getSales_channel());
        unprocessedOrderEntity.setPriority(orderDTO.getPriority());
        unprocessedOrderEntity.setDate(orderDTO.getDate());
        unprocessedOrderEntity.setShipDate(orderDTO.getShip_date());
        unprocessedOrderEntity.setUnitsSold(orderDTO.getUnits_sold());
        unprocessedOrderEntity.setUnitPrice(orderDTO.getUnit_price());
        unprocessedOrderEntity.setUnitCost(orderDTO.getUnit_cost());
        unprocessedOrderEntity.setTotalRevenue(orderDTO.getTotal_revenue());
        unprocessedOrderEntity.setTotalCost(orderDTO.getTotal_cost());
        unprocessedOrderEntity.setTotalProfit(orderDTO.getTotal_profit());

        return unprocessedOrderEntity;
    }

    static OrderEntity fromUnprocessedToProcessed(UnprocessedOrderEntity unprocessedOrderEntity) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(unprocessedOrderEntity.getId());
        orderEntity.setUuid(UUID.fromString(unprocessedOrderEntity.getUuid()));
        orderEntity.setRegion(unprocessedOrderEntity.getRegion());
        orderEntity.setCountry(unprocessedOrderEntity.getCountry());
        orderEntity.setItemType(unprocessedOrderEntity.getItemType());
        orderEntity.setSalesChannel(unprocessedOrderEntity.getSalesChannel());
        orderEntity.setPriority(unprocessedOrderEntity.getPriority());
        orderEntity.setDate(LocalDate.parse(unprocessedOrderEntity.getDate(), formatter));
        orderEntity.setShipDate(LocalDate.parse(unprocessedOrderEntity.getShipDate(), formatter));
        orderEntity.setUnitsSold(Integer.parseInt(unprocessedOrderEntity.getUnitsSold()));
        orderEntity.setUnitPrice(BigDecimal.valueOf(Double.parseDouble(unprocessedOrderEntity.getUnitPrice())));
        orderEntity.setUnitCost(BigDecimal.valueOf(Double.parseDouble(unprocessedOrderEntity.getUnitCost())));
        orderEntity.setTotalRevenue(BigDecimal.valueOf(Double.parseDouble(unprocessedOrderEntity.getTotalRevenue())));
        orderEntity.setTotalCost(BigDecimal.valueOf(Double.parseDouble(unprocessedOrderEntity.getTotalCost())));
        orderEntity.setTotalProfit(BigDecimal.valueOf(Double.parseDouble(unprocessedOrderEntity.getTotalProfit())));

        return orderEntity;
    }
}