package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.OrderEntity;
import com.alaguna.orderkata.repository.UnprocessedOrderEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskProcessOrdersHelperTest {

    @InjectMocks
    private TaskProcessOrdersHelper taskProcessOrdersHelper;

    @Mock
    private EntityManager entityManager;

    @Test
    void when_process_data_correctly_should_save_process_orders ()  {

        UnprocessedOrderEntity unprocessOrder = OrdersMother.getUnprocessOrder();
        UnprocessedOrderEntity unprocessOrderExpected = OrdersMother.getUnprocessOrder();
        unprocessOrderExpected.setProcessed(true);
        OrderEntity order = OrdersMother.getOrder();

        taskProcessOrdersHelper.process(unprocessOrder);

        verify(entityManager).persist(order);
        verify(entityManager).merge(unprocessOrderExpected);
    }

}
