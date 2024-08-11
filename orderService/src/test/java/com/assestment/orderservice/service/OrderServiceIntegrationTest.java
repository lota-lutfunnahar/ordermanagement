package com.assestment.orderservice.service;

import com.assestment.orderservice.entity.Order;
import com.assestment.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = { "order.created", "order.processed" })
public class OrderServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private KafkaTemplate<String, Order> kafkaTemplate;

    @BeforeEach
    void setup() {
        Order order = new Order();
        order.setId(1L);
        order.setProductId(123L);
        order.setQuantity(10);
        order.setStatus("CREATED");

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderRepository.findById(1L)).thenReturn(java.util.Optional.of(order));
    }

    @Test
    public void testCreateOrder() throws Exception {
        mockMvc.perform(post("/orders")
                        .contentType("application/json")
                        .content("{\"productId\":123,\"quantity\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(123))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(kafkaTemplate).send(eq("order.created"), any(Order.class));
    }
    @Test
    public void testGetOrder() throws Exception {
        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(123))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

}
