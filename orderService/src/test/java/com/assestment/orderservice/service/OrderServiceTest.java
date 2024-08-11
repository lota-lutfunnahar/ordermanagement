package com.assestment.orderservice.service;
import com.assestment.orderservice.entity.Order;
import com.assestment.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaTemplate<String, Order> kafkaTemplate;

    @InjectMocks
    private OrderService orderService;

    public OrderServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setProductId(123L);
        order.setQuantity(10);
        order.setStatus("CREATED");

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        verify(orderRepository).save(order);
        verify(kafkaTemplate).send("order.created", createdOrder);
        assert createdOrder.getStatus().equals("CREATED");
    }

    @Test
    public void testConsumeProcessedOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("FULFILLED");

        when(orderRepository.findById(order.getId())).thenReturn(java.util.Optional.of(order));

        orderService.consumeProcessedOrder(order, "order.processed");

        verify(orderRepository).save(order);
        assert order.getStatus().equals("FULFILLED");
    }
}
