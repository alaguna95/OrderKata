package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.*;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
class TaskProcessOrdersHelper {

    private final EntityManager entityManager;

    @Transactional
    void process(final UnprocessedOrderEntity unprocessedOrderEntity) {

        final OrderEntity orderEntity = OrderMapper.fromUnprocessedToProcessed(unprocessedOrderEntity);
        this.entityManager.persist(orderEntity);
        unprocessedOrderEntity.setProcessed(true);
        this.entityManager.merge(unprocessedOrderEntity);
    }

}