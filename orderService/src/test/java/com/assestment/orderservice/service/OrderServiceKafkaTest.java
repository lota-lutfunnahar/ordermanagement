package com.assestment.orderservice.service;

import com.assestment.orderservice.entity.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "order.created", "order.processed" })
@DirtiesContext
public class OrderServiceKafkaTest {
    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;

    private CountDownLatch latch;
    private Order consumedOrder;

    @BeforeEach
    public void setup() {
        latch = new CountDownLatch(1);
    }

    @KafkaListener(topics = "order.created", groupId = "testGroup")
    public void consumeOrderCreated(ConsumerRecord<String, Order> record) {
        consumedOrder = record.value();
        latch.countDown();
    }
    @Test
    public void testOrderCreationEventPublishing() throws Exception {
        Order order = new Order();
        order.setId(1L);
        order.setProductId(123L);
        order.setQuantity(10);
        order.setStatus("CREATED");

        // Send to Kafka
        kafkaTemplate.send("order.created", order);
        boolean messageConsumed = latch.await(10, TimeUnit.SECONDS);

        // Verify that the message
        assertThat(messageConsumed).isTrue();
        assertThat(consumedOrder).isNotNull();
        assertThat(consumedOrder.getStatus()).isEqualTo("CREATED");
    }
}
