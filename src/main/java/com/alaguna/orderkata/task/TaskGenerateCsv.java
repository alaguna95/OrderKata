package com.alaguna.orderkata.task;

import com.alaguna.orderkata.repository.OrderEntity;
import com.alaguna.orderkata.repository.OrderRepository;
import com.opencsv.CSVWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
class TaskGenerateCsv extends Task implements CommandLineRunner {

    static final String TASK_GENERATE_CSV_NAME = "generateCsv";

    private final OrderRepository orderRepository;

    @Override
    public void run(final String... args) {

        final LocalDateTime startTaskTime = init(TASK_GENERATE_CSV_NAME, args);
        if(startTaskTime == null){
            return;
        }

        final Map<String, Integer> regionResume = new HashMap<>();
        final Map<String, Integer> countryResume = new HashMap<>();
        final Map<String, Integer> itemTypeResume = new HashMap<>();
        final Map<String, Integer> salesChannelResume = new HashMap<>();
        final Map<String, Integer> orderPriorityResume = new HashMap<>();

        writeOrdersToCsv(orderRepository.findAll(Sort.by(Sort.Direction.DESC, "id")), regionResume, countryResume, itemTypeResume,
                salesChannelResume, orderPriorityResume);

        writeResume(regionResume, "Region");
        writeResume(countryResume,"Country");
        writeResume(itemTypeResume,"Item Type");
        writeResume(salesChannelResume,"Sales Channel");
        writeResume(orderPriorityResume, "Order Priority");

        finish(TASK_GENERATE_CSV_NAME, startTaskTime);
    }

    private void writeOrdersToCsv(
            final List<OrderEntity> orders,
            final Map<String, Integer> regionResume,
            final Map<String, Integer> countryResume,
            final Map<String, Integer> itemsType,
            final Map<String, Integer> salesChannelResume,
            final Map<String, Integer> orderPriorityResume) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try (final CSVWriter writer = new CSVWriter(new FileWriter("orders.csv"))) {

            writer.writeNext(new String[] {
                    "UUID", "ID", "Priority", "Date", "Region", "Country", "Item Type", "Sales Channel", "Ship Date",
                    "Units Sold", "Unit Price", "Unit Cost", "Total Revenue", "Total Cost", "Total Profit"  });
            orders.forEach(order -> {
                String[] data = {
                        order.getUuid().toString(),
                        order.getId(),
                        order.getPriority(),
                        order.getDate().format(formatter),
                        order.getRegion(),
                        order.getCountry(),
                        order.getItemType(),
                        order.getSalesChannel(),
                        order.getShipDate().format(formatter),
                        String.valueOf(order.getUnitsSold()),
                        String.valueOf(order.getUnitPrice()),
                        String.valueOf(order.getUnitCost()),
                        String.valueOf(order.getTotalRevenue()),
                        String.valueOf(order.getTotalCost()),
                        String.valueOf(order.getTotalProfit())
                };
                writer.writeNext(data);

                regionResume.put(order.getRegion(), regionResume.getOrDefault(order.getRegion(), 0) + 1);
                countryResume.put(order.getCountry(), countryResume.getOrDefault(order.getCountry(), 0) + 1);
                itemsType.put(order.getItemType(), itemsType.getOrDefault(order.getItemType(), 0) + 1);
                salesChannelResume.put(order.getSalesChannel(),
                        salesChannelResume.getOrDefault(order.getSalesChannel(), 0) + 1);
                orderPriorityResume.put(order.getPriority(),
                        orderPriorityResume.getOrDefault(order.getPriority(), 0) + 1);
            });

        } catch (IOException e) {
            log.error("Error writing orders to csv", e);
        }
    }

    private void writeResume(
            final Map<String, Integer> resume,
            final String header) {

        log.info("***************************************");
        log.info("Writing {} resume", header);
        resume.forEach((key, value) -> log.info("{}: {}; Count: {}", header, key, value));
        log.info("***************************************");
    }
}