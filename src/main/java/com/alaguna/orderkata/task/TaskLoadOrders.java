package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.UnprocessedOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
@Slf4j
class TaskLoadOrders extends Task implements CommandLineRunner {

    static final String TASK_LOAD_ORDERS_NAME = "loadOrders";

    private static final int MIN_PAGE = 1;

    private static final int MAX_PAGE = 10000;

    private static final String URI = "https://kata-espublicotech.g3stiona.com/v1/orders";

    private final ExecutorService executorService = Executors.newFixedThreadPool(8);

    private final RetryTemplate retryTemplate = RetryTemplate.builder()
            .maxAttempts(10)
            .fixedBackoff(2000)
            .build();

    private final RestTemplate restTemplate;

    private final UnprocessedOrderRepository unprocessedOrderRepository;

    @Override
    public void run(final String... args) throws Exception {

        final LocalDateTime startTaskTime = init(TASK_LOAD_ORDERS_NAME, args);
        if(startTaskTime == null){
            return;
        }

        final int initPage = calculateInitPage(args);
        final int lastPage = calculateLastPage(initPage, args);

        final CountDownLatch latch = new CountDownLatch(lastPage-initPage+1);
        IntStream.rangeClosed(initPage, lastPage).forEach(pageNumber -> processPage(pageNumber, latch));
        latch.await();

        finish(TASK_LOAD_ORDERS_NAME, startTaskTime);
    }

    private void processPage(
            final int pageNumber,
            final CountDownLatch latch) {

        executorService.submit(() -> {
            try {
                ResponseOrders result = retryTemplate.execute(context -> restTemplate
                        .getForObject(URI + "?page=" + pageNumber, ResponseOrders.class));
                var orders = result.getContent().stream().map(OrderMapper::fromDTOToUnprocessed).toList();
                orders.forEach(unprocessedOrderRepository::save);

            }catch (Exception e) {
                log.error("Error in page {}", pageNumber);
            }finally {
                latch.countDown();
            }
        });
    }

    private int calculateInitPage(final String... args){

        if(args.length == 1){
            return MIN_PAGE;
        }
        final int initPage;
        try{
            initPage = Integer.parseInt(args[1]);
        }catch (Exception e){
            log.error("Error parsing init page", e);
            throw new IllegalArgumentException("Invalid init page value, value: " + args[1]);
        }

        if(initPage < MIN_PAGE){
            log.error("The minimum value for init page is {}", MIN_PAGE);
            throw new IllegalArgumentException("The minimum value for init page is " + MIN_PAGE);
        }
        if(initPage > MAX_PAGE){
            log.error("The maximum value for init page is {}", MAX_PAGE);
            throw new IllegalArgumentException("The minimum value for init page is " + MIN_PAGE);
        }
        return initPage;
    }

    private int calculateLastPage(
            final int initPage,
            final String... args) {

        if (args.length == 1 || args.length == 2) {
            return MAX_PAGE;
        }
        final int lastPage;
        try {
            lastPage = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.out.println("Error parsing init page");
            throw new IllegalArgumentException("Invalid last page value, value: "+ args[2]);
        }
        if (lastPage < MIN_PAGE) {
            log.error("The minimum value for last page is {}", MIN_PAGE);
            throw new IllegalArgumentException("The minimum value for last page is " + MIN_PAGE);
        }
        if (lastPage > MAX_PAGE) {
            log.error("The maximum value for last page is {}", MAX_PAGE);
            throw new IllegalArgumentException("The maximum value for last page is 1" + MAX_PAGE);
        }
        if (lastPage < initPage) {
            log.error("The last page must be greater or equals than the init page");
            throw new IllegalArgumentException("The last page must be greater or equals than the init page");
        }
        return lastPage;
    }

}