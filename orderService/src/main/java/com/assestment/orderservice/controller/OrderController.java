package com.assestment.orderservice.controller;

import com.assestment.orderservice.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.assestment.orderservice.service.OrderService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order){
        log.info("Received request of order {}", order);
        Order response = orderService.createOrder(order);
        log.info("Received response of request order {}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order>  getOrder(@PathVariable Long id) {
        log.info("Received request of order id {}", id);
        Order response =  orderService.getOrderById(id);
        if (response == null) {
            log.warn("No data found {} ", response);
            return ResponseEntity.noContent().build();
        }
        log.info("Received response of request id {}", response);
        return ResponseEntity.ok(response);
    }
}
