package com.assestment.inventoryservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic orderCreatedTopic() {
        return new NewTopic("order.created", 1, (short) 1);
    }

    @Bean
    public NewTopic orderProcessedTopic() {
        return new NewTopic("order.processed", 1, (short) 1);
    }
}
