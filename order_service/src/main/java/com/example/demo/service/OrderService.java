package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.OrderRequest;
import com.example.demo.model.Order;

@Service
@Transactional
public interface OrderService {

	public Order placeOrder(OrderRequest orderRequest);
}
