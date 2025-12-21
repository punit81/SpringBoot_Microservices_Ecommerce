package com.productservice.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.productservice.dto.ProductRequestDto;
import com.productservice.dto.ProductResponseDto;
import com.productservice.model.Product;
import com.productservice.repo.ProductRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepo productRepo;
	
	public List<ProductResponseDto> getAllProducts() {
		return productRepo.findAll().stream()
				.map(this::mapToProductResponseDto)
				.toList();
	}
	
	public void createProduct(ProductRequestDto productRequestDto) {
		Product product = Product.builder()
				.productName(productRequestDto.getProductName())
				.description(productRequestDto.getDescription())
				.price(productRequestDto.getPrice())
				.id(UUID.randomUUID().toString())
				.skuCode(productRequestDto.getSkuCode())
				.build();
		productRepo.save(product);
	}
	
	public void updateProduct(ProductRequestDto productRequestDto,String ProductId) {
		Product product = productRepo.findById(ProductId).orElse(null);
		if (product == null) {
			throw new RuntimeException("Product not found");
		}
		else {
			product.setProductName(productRequestDto.getProductName());
			product.setDescription(productRequestDto.getDescription());
			product.setPrice(productRequestDto.getPrice());
			product.setSkuCode(productRequestDto.getSkuCode());
			productRepo.save(product);
		}
	}
	
	public ProductResponseDto getProductById(String productId) {
		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));
		return mapToProductResponseDto(product);
	}
	
	public ProductResponseDto getProductByName(String productName) {
		Product product = productRepo.findByProductName(productName);
		if (product == null) {
			throw new RuntimeException("Product not found");
		}
		return mapToProductResponseDto(product);
	}
	
	public ProductResponseDto mapToProductResponseDto(Product product) {
		return ProductResponseDto.builder()
				.id(product.getId())
				.name(product.getProductName())
				.description(product.getDescription())
				.price(product.getPrice())
				.skuCode(product.getSkuCode())
				.build();
	}
	public void deleteProduct(String productId) {
		productRepo.deleteById(productId);
	}
	
}
