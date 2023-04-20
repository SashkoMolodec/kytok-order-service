package com.kytokvinily.domain;

import com.kytokvinily.vinyl.VinylClient;
import com.kytokvinily.vinyl.VinylDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {
    private final VinylClient vinylClient;
    private final OrderRepository orderRepository;

    public OrderService(VinylClient vinylClient, OrderRepository orderRepository) {
        this.vinylClient = vinylClient;
        this.orderRepository = orderRepository;
    }

    public Flux<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Mono<Order> submitOrder(Long id, int quantity) {
        return vinylClient.getVinylById(id)
                .map(vinylDto -> buildAcceptedOrder(vinylDto, quantity))
                .defaultIfEmpty(buildRejectedOrder(id, quantity))
                .flatMap(orderRepository::save);
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
}
