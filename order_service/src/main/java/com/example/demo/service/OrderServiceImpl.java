package com.example.demo.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

import com.example.demo.dto.InventoryResponse;
import com.example.demo.dto.OrderLineItemsDto;
import com.example.demo.dto.OrderRequest;
import com.example.demo.event.OrderPlacedEvent;
import com.example.demo.model.Order;
import com.example.demo.model.OrderLineItems;
import com.example.demo.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;
	private final WebClient.Builder webClientBuilder;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository, Builder webClientBuilder, KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate) {
		super();
		this.orderRepository = orderRepository;
		this.webClientBuilder = webClientBuilder;
		this.kafkaTemplate = kafkaTemplate;
	}

	@Override
	public Order placeOrder(OrderRequest orderRequest) {
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
				.map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).collect(Collectors.toList());
		order.setOrderLineItems(orderLineItems);

		List<String> skuCodes = order.getOrderLineItems().stream().map(orderLineItem -> orderLineItem.getSkuCode())
				.collect(Collectors.toList());

		// Call Inventory service and place order, if product is in stock
		InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
				.uri("http://inventory-service/api/inventory",
						uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
				.retrieve().bodyToMono(InventoryResponse[].class).block();

		boolean allProductsInStock = Arrays.stream(inventoryResponses)
				.allMatch(inventoryResponse -> inventoryResponse.getIsInStock());

		if (allProductsInStock) {
			orderRepository.save(order);
			kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
		} else {
			throw new IllegalArgumentException("Product is not in Stock, Please try again later");
		}
		return order;
	}

	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		return orderLineItems;
	}

}
