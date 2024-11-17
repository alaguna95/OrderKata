package com.alaguna.orderkata.repository;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnprocessedOrderRepository extends JpaRepository<UnprocessedOrderEntity, String> {

    List<UnprocessedOrderEntity> findByProcessed(boolean processed);

}