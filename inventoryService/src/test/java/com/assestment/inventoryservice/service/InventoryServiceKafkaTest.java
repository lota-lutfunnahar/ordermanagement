package com.assestment.inventoryservice.service;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "order.created", "order.processed" })
@DirtiesContext
public class InventoryServiceKafkaTest {

}
