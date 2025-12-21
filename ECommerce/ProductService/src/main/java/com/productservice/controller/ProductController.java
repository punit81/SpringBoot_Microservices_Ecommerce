package com.productservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.productservice.dto.ProductRequestDto;
import com.productservice.dto.ProductResponseDto;
import com.productservice.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
	private final ProductService productService;
	
	@GetMapping("/products")
	@ResponseStatus(HttpStatus.OK)
	public List<ProductResponseDto> getAllProducts() {
		return productService.getAllProducts();
	}
	
	@PostMapping("/products")
	@ResponseStatus(HttpStatus.CREATED)
	public void createProduct(@RequestBody ProductRequestDto productRequestDto) {
		productService.createProduct(productRequestDto);
	}
	
	@GetMapping("/products/by-name")
	@ResponseStatus(HttpStatus.OK)
	public ProductResponseDto getProductByName(@RequestBody String productName) {
		return productService.getProductByName(productName);
	}
	
	@PutMapping("/products/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void updateProduct(@RequestBody ProductRequestDto productRequestDto, @PathVariable String id) {
		 productService.updateProduct(productRequestDto,id);
	}
	
	@DeleteMapping("/products/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteProduct(@PathVariable String id) {
		productService.deleteProduct(id);
	}
}
