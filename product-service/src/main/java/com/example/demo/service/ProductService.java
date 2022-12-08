package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.model.Product;

public interface ProductService {

	public Product createProduct(ProductRequest productRequest);

	public List<ProductResponse> findAll();

	public ProductResponse findById(String id);
}
