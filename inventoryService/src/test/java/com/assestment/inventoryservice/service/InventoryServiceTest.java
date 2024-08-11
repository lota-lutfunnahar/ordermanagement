package com.assestment.inventoryservice.service;


import com.assestment.inventoryservice.entity.Inventory;
import com.assestment.inventoryservice.entity.Order;
import com.assestment.inventoryservice.repository.InventoryRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private KafkaTemplate<String, Order> kafkaTemplate;

    @InjectMocks
    private InventoryService inventoryService;

    public InventoryServiceTest() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testConsumeOrderCreated() {
        Inventory inventory = new Inventory();
        inventory.setProductId(123L);
        inventory.setQuantity(20);

        Order order = new Order();
        order.setProductId(123L);
        order.setQuantity(10);


        when(inventoryRepository.findByProductId(order.getProductId())).thenReturn(inventory);

//        inventoryService.consumeOrderCreated(new ConsumerRecord<>(record)) ;

        verify(inventoryRepository).save(any(Inventory.class));
        verify(kafkaTemplate).send("order.processed", order);
        assert order.getStatus().equals("FULFILLED");
    }
}
