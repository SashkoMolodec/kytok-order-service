package com.kytokvinily.domain;

import com.kytokvinily.event.OrderAcceptedMessage;
import com.kytokvinily.event.OrderDispatchedMessage;
import com.kytokvinily.vinyl.VinylClient;
import com.kytokvinily.vinyl.VinylDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.cloud.stream.function.StreamBridge;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final VinylClient vinylClient;
    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge;

    public OrderService(VinylClient vinylClient, OrderRepository orderRepository, StreamBridge streamBridge) {
        this.vinylClient = vinylClient;
        this.orderRepository = orderRepository;
        this.streamBridge = streamBridge;
    }

    public Flux<Order> getAllOrders(String userId) {
        return orderRepository.findAllByCreatedBy(userId);
    }

    @Transactional
    public Mono<Order> submitOrder(Long id, int quantity) {
        return vinylClient.getVinylById(id)
                .map(vinyl -> buildAcceptedOrder(vinyl, quantity))
                .defaultIfEmpty(buildRejectedOrder(id, quantity))
                .flatMap(orderRepository::save)
                .doOnNext(this::publishOrderAcceptedEvent);
    }

    private Order buildAcceptedOrder(VinylDto vinylDto, int quantity) {
        return Order.of(vinylDto.getId(),
                vinylDto.getTitle(),
                vinylDto.getAuthor(),
                100.0,
                quantity,
                OrderStatus.ACCEPTED);
    }

    public static Order buildRejectedOrder(Long vinylId, int quantity) {
        return Order.of(vinylId, null, null, null, quantity, OrderStatus.REJECTED);
    }

    private void publishOrderAcceptedEvent(Order order) {
        if (!order.orderStatus().equals(OrderStatus.ACCEPTED)) {
            return;
        }
        var orderAcceptedMessage = new OrderAcceptedMessage(order.id());
        log.info("Sending order accepted event with id: {}", order.id());
        var result = streamBridge.send("acceptOrder-out-0", orderAcceptedMessage);
        log.info("Result of sending data for order with id {}: {}", order.id(), result);
    }

    public Flux<Order> consumeOrderDispatchedEvent(Flux<OrderDispatchedMessage> flux) {
        return flux
                .flatMap(message -> orderRepository.findById(message.orderId()))
                .map(this::buildDispatchedOrder)
                .flatMap(orderRepository::save);
    }

    private Order buildDispatchedOrder(Order existingOrder) {
        return new Order(
                existingOrder.id(),
                existingOrder.vinylId(),
                existingOrder.vinylTitle(),
                existingOrder.vinylAuthor(),
                existingOrder.vinylPrice(),
                existingOrder.quantity(),
                OrderStatus.DISPATCHED,
                existingOrder.createdDate(),
                existingOrder.lastModifiedDate(),
                existingOrder.createdBy(),
                existingOrder.lastModifiedBy(),
                existingOrder.version()
        );
    }
}
