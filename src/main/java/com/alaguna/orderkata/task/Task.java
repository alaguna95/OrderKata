package com.alaguna.orderkata.task;

import lombok.extern.slf4j.Slf4j;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.alaguna.orderkata.task.TaskGenerateCsv.TASK_GENERATE_CSV_NAME;
import static com.alaguna.orderkata.task.TaskLoadOrders.TASK_LOAD_ORDERS_NAME;
import static com.alaguna.orderkata.task.TaskProcessOrders.TASK_PROCESS_ORDERS_NAME;

@Slf4j
class Task{

    protected LocalDateTime init(
            final String taskName,
            final String... args) {

        if (args.length == 0 || !List.of(TASK_LOAD_ORDERS_NAME, TASK_PROCESS_ORDERS_NAME, TASK_GENERATE_CSV_NAME).contains(args[0])){
            System.exit(0);
        }

        if(args[0].equals(taskName)){
            log.info("Init task {}", taskName);
            log.info("Arguments: {}", Arrays.toString(args));
            return LocalDateTime.now();
        }

        return null;
    }

    protected void finish(
            final String taskName,
            final LocalDateTime startTaskTime) {

        final LocalDateTime endTaskTime = LocalDateTime.now();
        log.info("Task {} finished, the duration was: {} seconds", taskName, Duration.between(startTaskTime, endTaskTime).getSeconds());
        System.exit(0);
    }
}