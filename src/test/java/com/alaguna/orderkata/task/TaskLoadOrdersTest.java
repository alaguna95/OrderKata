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
import org.springframework.web.client.RestTemplate;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskLoadOrdersTest {

    private static final int MIN_PAGE = 1;

    private static final int MAX_PAGE = 10000;

    @InjectMocks
    private TaskLoadOrders taskLoadOrders;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UnprocessedOrderRepository unprocessedOrderRepository;

    @Test
    void when_load_data_correctly_should_save_unprocessed_orders () throws Exception {

        ResponseOrders responseOrders = OrdersMother.getResponseOrders();
        UnprocessedOrderEntity orderToSave = OrdersMother.getUnprocessOrder();

        when(restTemplate.getForObject("https://kata-espublicotech.g3stiona.com/v1/orders?page=4",ResponseOrders.class))
                .thenReturn(responseOrders);

        SystemLambda.catchSystemExit(() -> taskLoadOrders.run("loadOrders", "4", "4"));

        verify(unprocessedOrderRepository).save(orderToSave);
    }

    @ParameterizedTest
    @CsvSource({"processOrders", "generateCsv"})
    void when_sent_invalid_task_name_should_do_nothing (String taskName) throws Exception {

        taskLoadOrders.run(taskName);

        verifyNoInteractions(restTemplate, unprocessedOrderRepository);
    }

    @Test
    void when_sent_invalid_task_name_should_stop_the_server() throws Exception {

        SystemLambda.catchSystemExit(() -> taskLoadOrders.run("invalidTaskName"));

        verifyNoInteractions(restTemplate, unprocessedOrderRepository);
    }


    @Test
    void when_do_not_sent_arguments_should_stop_the_server() throws Exception {

        SystemLambda.catchSystemExit(() -> taskLoadOrders.run());

        verifyNoInteractions(restTemplate, unprocessedOrderRepository);
    }

    @Test
    void when_load_data_and_external_call_fail_should_retry_10_times () throws Exception {

        when(restTemplate.getForObject("https://kata-espublicotech.g3stiona.com/v1/orders?page=10000",ResponseOrders.class))
                .thenThrow(new RuntimeException("Error"));

        SystemLambda.catchSystemExit(() -> taskLoadOrders.run("loadOrders","10000"));

        verify(restTemplate, times(10)).getForObject("https://kata-espublicotech.g3stiona.com/v1/orders?page=10000",
                ResponseOrders.class);
    }

    @Test
    void when_init_page_arg_is_less_than_one_should_throw_exception () {

        assertThatThrownBy(() -> taskLoadOrders.run("loadOrders","0"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The minimum value for init page is " + MIN_PAGE);
    }

    @Test
    void when_init_page_arg_is_more_than_10000_should_throw_exception () {

        assertThatThrownBy(() -> taskLoadOrders.run("loadOrders","10001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The minimum value for init page is " + MIN_PAGE);
    }

    @Test
    void when_init_page_arg_is_not_a_number_should_throw_exception () {

        assertThatThrownBy(() -> taskLoadOrders.run("loadOrders","S"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid init page value, value: S");
    }

    @Test
    void when_last_page_arg_is_less_than_one_should_throw_exception () {

        assertThatThrownBy(() -> taskLoadOrders.run("loadOrders","1","0"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The minimum value for last page is " + MIN_PAGE);
    }

    @Test
    void when_last_page_arg_is_more_than_10000_should_throw_exception () {

        assertThatThrownBy(() -> taskLoadOrders.run("loadOrders","1","10001"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The maximum value for last page is 1" + MAX_PAGE);
    }

    @Test
    void when_init_page_is_greater_than_last_page_should_throw_exception () {

        assertThatThrownBy(() -> taskLoadOrders.run("loadOrders","4","3"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The last page must be greater or equals than the init page");
    }

    @Test
    void when_last_page_arg_is_not_a_number_should_throw_exception () {

        assertThatThrownBy(() -> taskLoadOrders.run("loadOrders","1","S"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid last page value, value: S");
    }
}
