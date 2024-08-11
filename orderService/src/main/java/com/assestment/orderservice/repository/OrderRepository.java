package com.assestment.orderservice.repository;

import org.springframework.stereotype.Repository;
import com.assestment.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
