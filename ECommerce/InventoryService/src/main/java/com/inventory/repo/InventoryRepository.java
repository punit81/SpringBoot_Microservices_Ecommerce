package com.inventory.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inventory.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String> {

	Optional<Inventory> findBySkuCode(String code);

	List<Inventory> findBySkuCodeIn(List<String> skuCode);

}
