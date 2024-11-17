package com.alaguna.orderkata.task;

import lombok.Data;

import java.util.List;

@Data
class ResponseOrders {

    String page;

    List<OrderDTO> content;

}
