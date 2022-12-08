package com.example.demo.dto;

public class InventoryResponse {

	private String skuCode;
	private Boolean isInStock;
	public InventoryResponse() {
		super();
	}
	public InventoryResponse(String skuCode, Boolean isInStock) {
		super();
		this.skuCode = skuCode;
		this.isInStock = isInStock;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Boolean getIsInStock() {
		return isInStock;
	}
	public void setIsInStock(Boolean isInStock) {
		this.isInStock = isInStock;
	}
	@Override
	public String toString() {
		return "InventoryResponse [skuCode=" + skuCode + ", isInStock=" + isInStock + "]";
	}
	
}
