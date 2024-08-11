package com.assestment.inventoryservice.service;

import com.assestment.inventoryservice.entity.Inventory;
import com.assestment.inventoryservice.entity.Order;
import com.assestment.inventoryservice.repository.InventoryRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    private static final String ORDER_PROCESSED_TOPIC = "order.processed";

    @KafkaListener(topics = "order.created", groupId = "inventory_group")
    public void consumeOrderCreated(ConsumerRecord<String, Order> record) {
        Order order = record.value();
        Inventory inventory = inventoryRepository.findByProductId(order.getProductId());
        if (inventory != null && inventory.getQuantity() >= order.getQuantity()) {
            order.setStatus("FULFILLED");
            inventory.setQuantity(inventory.getQuantity() - order.getQuantity());
            inventoryRepository.save(inventory);
        } else {
            order.setStatus("FAILED");
        }
        kafkaTemplate.send(ORDER_PROCESSED_TOPIC, order);
    }

    public Inventory getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }
}
