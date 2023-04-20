package com.kytokvinily.web;

import com.kytokvinily.domain.Order;
import com.kytokvinily.domain.OrderService;
import com.kytokvinily.domain.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.BDDMockito.given;

@WebFluxTest(OrderController.class)
class OrderControllerWebFluxTests {

	@Autowired
	private WebTestClient webClient;

	@MockBean
	private OrderService orderService;

	@Test
	void whenVinylNotAvailableThenRejectOrder() {
		var orderRequest = new OrderRequest(111L, 3);
		var expectedOrder = OrderService.buildRejectedOrder(orderRequest.getVinylId(), orderRequest.getQuantity());
		given(orderService.submitOrder(orderRequest.getVinylId(), orderRequest.getQuantity()))
				.willReturn(Mono.just(expectedOrder));

		webClient
				.post()
				.uri("/orders")
				.bodyValue(orderRequest)
				.exchange()
				.expectStatus().is2xxSuccessful()
				.expectBody(Order.class).value(actualOrder -> {
					assertThat(actualOrder).isNotNull();
					assertThat(actualOrder.orderStatus()).isEqualTo(OrderStatus.REJECTED);
				});

	}

}
