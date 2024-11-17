package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
@RequiredArgsConstructor
@Slf4j
class TaskProcessOrders extends Task implements CommandLineRunner {

    static final String TASK_PROCESS_ORDERS_NAME = "processOrders";

    private final UnprocessedOrderRepository unprocessedOrderRepository;

    private final TaskProcessOrdersHelper taskProcessOrdersHelper;

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    @Override
    public void run(final String... args) throws InterruptedException {

        final LocalDateTime startTaskTime = init(TASK_PROCESS_ORDERS_NAME, args);
        if(startTaskTime == null){
            return;
        }

        final List<UnprocessedOrderEntity> orders = this.unprocessedOrderRepository.findByProcessed(false);
        final CountDownLatch latch = new CountDownLatch(orders.size());

        orders.forEach(unprocessedOrder ->
                    executorService.submit(() -> {
                        try {
                            taskProcessOrdersHelper.process(unprocessedOrder);
                        } catch (Exception e) {
                            log.error("Error processing unprocessed order with id: {}, with this error message: {}",
                                    unprocessedOrder.getId(), e.getMessage());
                            unprocessedOrder.setMessageError(e.getMessage().length() > 200
                                    ? e.getMessage().substring(0, 200)
                                    : e.getMessage());
                            unprocessedOrder.setProcessed(false);
                            unprocessedOrderRepository.save(unprocessedOrder);
                        } finally {
                            latch.countDown();
                        }
                    }));
        latch.await();

        finish(TASK_PROCESS_ORDERS_NAME, startTaskTime);
    }
}