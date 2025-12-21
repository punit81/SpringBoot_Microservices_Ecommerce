package com.productservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.productservice.model.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, String> {
	public Product findByProductName(String name);
}
