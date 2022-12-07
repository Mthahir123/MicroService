package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.InventoryResponse;
import com.example.demo.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService{

	private final InventoryRepository inventoryRepository;

	public InventoryServiceImpl(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<InventoryResponse> isInStock(List<String> skuCode) {
		return inventoryRepository.findBySkuCodeIn(skuCode).stream().map((inventory) -> {
			InventoryResponse inventoryResponse = new InventoryResponse();
			inventoryResponse.setSkuCode(inventory.getSkuCode());
			inventoryResponse.setIsInStock(inventory.getQuantity() > 0);
			return inventoryResponse;
		}).collect(Collectors.toList());
	}

}
