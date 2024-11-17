package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.OrderEntity;
import com.alaguna.orderkata.repository.OrderRepository;
import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskGenerateCsvTest {

    @InjectMocks
    private TaskGenerateCsv taskGenerateCsv;

    @Mock
    private OrderRepository orderRepository;


    @Test
    void when_call_generate_csv_task_should_generate_csv () throws Exception {

        OrderEntity orderEntity = OrdersMother.getOrder();

        when(orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(List.of(orderEntity));

        SystemLambda.catchSystemExit(() -> taskGenerateCsv.run("generateCsv"));
    }

    @ParameterizedTest
    @CsvSource({"loadOrders", "processOrders"})
    void when_sent_invalid_task_name_should_do_nothing (String taskName)  {

        taskGenerateCsv.run(taskName);

        verifyNoInteractions(orderRepository);
    }


    @Test
    void when_sent_invalid_task_name_should_stop_the_server() throws Exception {

        SystemLambda.catchSystemExit(() -> taskGenerateCsv.run("invalidTaskName"));

        verifyNoInteractions(orderRepository);
    }

    @Test
    void when_do_not_sent_arguments_should_stop_the_server() throws Exception {

        SystemLambda.catchSystemExit(() -> taskGenerateCsv.run());

        verifyNoInteractions(orderRepository);
    }

}
