package com.assestment.inventoryservice.service;

import com.assestment.inventoryservice.entity.Inventory;

import com.assestment.inventoryservice.repository.InventoryRepository;
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
public class InventoryServiceIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InventoryRepository inventoryRepository;

    @MockBean
    private KafkaTemplate<String, Inventory> kafkaTemplate;

    @BeforeEach
    void setup() {
        Inventory inventory = new Inventory();
        inventory.setId(1L);
        inventory.setProductId(123L);
        inventory.setQuantity(20);

        when(inventoryRepository.findByProductId(123L)).thenReturn(inventory);
    }


    @Test
    public void testGetInventory() throws Exception {
        mockMvc.perform(get("/inventory/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.productId").value(123))
                .andExpect(jsonPath("$.quantity").value(20));
    }
}
