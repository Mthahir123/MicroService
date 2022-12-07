package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.InventoryResponse;

@Service
public interface InventoryService {

	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode);
}
