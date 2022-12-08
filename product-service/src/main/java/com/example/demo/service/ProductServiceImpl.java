package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{

	private final ProductRepository productRepository;
	Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);
	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product createProduct(ProductRequest productRequest) {
		Product product = new Product();
		product.setName(productRequest.getName());
		product.setDescription(productRequest.getDescription());
		product.setPrice(productRequest.getPrice());
		
		productRepository.save(product);
		logger.info("Product Saved with Id : "+product.getId());
		return product;
	}

	@Override
	public List<ProductResponse> findAll() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(product -> mapToProductResponse(product)).collect(Collectors.toList());
	}

	private ProductResponse mapToProductResponse(Product product) {
		ProductResponse productResponse = new ProductResponse();
		productResponse.setId(product.getId());
		productResponse.setName(product.getName());
		productResponse.setDescription(product.getDescription());
		productResponse.setPrice(product.getPrice());
		return productResponse;
	}

	@Override
	public ProductResponse findById(String id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("The Data not found with id : "+ id));
		return mapToProductResponse(product);
	}
}
