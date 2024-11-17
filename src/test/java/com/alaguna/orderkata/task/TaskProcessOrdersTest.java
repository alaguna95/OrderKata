package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.UnprocessedOrderEntity;
import com.alaguna.orderkata.repository.UnprocessedOrderRepository;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskProcessOrdersTest {

    @InjectMocks
    private TaskProcessOrders taskProcessOrders;

    @Mock
    private UnprocessedOrderRepository unprocessedOrderRepository;

    @Mock
    private TaskProcessOrdersHelper taskProcessOrdersHelper;


    @Test
    void when_process_data_correctly_should_save_process_orders () throws Exception {

        UnprocessedOrderEntity orderSaved = OrdersMother.getUnprocessOrder();

        when(unprocessedOrderRepository.findByProcessed(false))
                .thenReturn(List.of(orderSaved));

        SystemLambda.catchSystemExit(() -> taskProcessOrders.run("processOrders"));

        verify(taskProcessOrdersHelper).process(orderSaved);
        verifyNoMoreInteractions(taskProcessOrdersHelper, unprocessedOrderRepository);
    }

    @Test
    void when_process_data_incorrectly_should_save_error_in_unprocessed_order () throws Exception {

        UnprocessedOrderEntity orderSaved = OrdersMother.getUnprocessOrder();
        UnprocessedOrderEntity orderSavedExpected = OrdersMother.getUnprocessOrder();
        orderSavedExpected.setMessageError("Error processing order");

        when(unprocessedOrderRepository.findByProcessed(false))
                .thenReturn(List.of(orderSaved));
        doThrow(new RuntimeException("Error processing order")).when(taskProcessOrdersHelper).process(orderSaved);

        SystemLambda.catchSystemExit(() -> taskProcessOrders.run("processOrders"));

        verify(unprocessedOrderRepository).save(orderSavedExpected);
        verifyNoMoreInteractions(taskProcessOrdersHelper, unprocessedOrderRepository);
    }


    @ParameterizedTest
    @CsvSource({"loadOrders", "generateCsv"})
    void when_sent_invalid_task_name_should_do_nothing (String taskName) throws Exception {

        taskProcessOrders.run(taskName);

        verifyNoInteractions(taskProcessOrdersHelper, unprocessedOrderRepository);
    }


    @Test
    void when_sent_invalid_task_name_should_stop_the_server () throws Exception {

        SystemLambda.catchSystemExit(() -> taskProcessOrders.run("invalidTaskName"));

        verifyNoInteractions(taskProcessOrdersHelper, unprocessedOrderRepository);
    }

    @Test
    void when_do_not_sent_arguments_should_stop_the_server() throws Exception {

        SystemLambda.catchSystemExit(() -> taskProcessOrders.run());

        verifyNoInteractions(taskProcessOrdersHelper, unprocessedOrderRepository);
    }
}
