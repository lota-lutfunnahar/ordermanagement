package com.assestment.orderservice.service;

import com.assestment.orderservice.entity.Order;
import com.assestment.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;


@Service
@Slf4j
public class OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    private static final String ORDER_CREATED_TOPIC = "order.created";
    private static final String ORDER_PROCESSED_TOPIC = "order.processed";

    public Order createOrder(Order order){
        order.setStatus("CREATED");
        order = orderRepository.save(order);
        log.info("Successfully saved {}", order);
        kafkaTemplate.send(ORDER_CREATED_TOPIC, order);
        log.info("Send order create event");
        return order;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @KafkaListener(topics = "order.processed", groupId = "order_group")
    public void consumeProcessedOrder(@Payload Order order,
                                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        if (ORDER_PROCESSED_TOPIC.equals(topic)) {
            Order existingOrder = orderRepository.findById(order.getId()).orElse(null);
            if (existingOrder != null) {
                existingOrder.setStatus(order.getStatus());
                orderRepository.save(existingOrder);
            }
        }
    }
}
