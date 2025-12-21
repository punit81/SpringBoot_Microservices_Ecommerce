package com.inventory.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.inventory.dto.InventoryRequestDto;
import com.inventory.dto.InventoryResponseDto;
import com.inventory.model.Inventory;
import com.inventory.repo.InventoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryService {
	private final InventoryRepository inventoryRepository;
	//See controller and implement methods here
	
	public void createInventory(InventoryRequestDto requestDto) {
		Inventory inventory = Inventory.builder()
				.productName(requestDto.getProductName())
				.skuCode(requestDto.getSkuCode())
				.quantity(requestDto.getQuantity())
				.id(UUID.randomUUID().toString())
				.build();
		inventoryRepository.save(inventory);
		
	}
	
	public void deleteInventory(String id) {
		inventoryRepository.deleteById(id);
	}
	
	public void updateInventory(String id, InventoryRequestDto requestDto) {
		Inventory inventory = inventoryRepository.findById(id).orElse(null);
		if (inventory == null) {
			throw new RuntimeException("Inventory not found");
		}
		else {
			inventory.setProductName(requestDto.getProductName());
			inventory.setSkuCode(requestDto.getSkuCode());
			inventory.setQuantity(requestDto.getQuantity());
			inventoryRepository.save(inventory);
		}
	}
	
	public InventoryResponseDto getInventoryById(String id) {
		Inventory inventory = inventoryRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Inventory not found"));
		return InventoryResponseDto.builder()
				.id(inventory.getId())
				.productName(inventory.getProductName())
				.skuCode(inventory.getSkuCode())
				.quantity(inventory.getQuantity())
				.build();
	}
	
	public java.util.List<InventoryResponseDto> getAllInventories() {
		return inventoryRepository.findAll().stream()
				.map(this::mapToInventoryResponseDto)
				.toList();
	}
	
	private InventoryResponseDto mapToInventoryResponseDto(Inventory inventory) {
		return InventoryResponseDto.builder()
				.id(inventory.getId())
				.productName(inventory.getProductName())
				.skuCode(inventory.getSkuCode())
				.quantity(inventory.getQuantity())
				.build();
	}

	//return a map of skuCode and boolean indicating availability
	//if item is not in inventory or quantity=0 then <skuCode, false>
    public Map<String, Boolean> getAvailabilityBySkuCodes(List<String> skuCodes) {
		List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
		Map<String, Integer> skuCodeToQuantity = inventories.stream()
				.collect(Collectors.toMap(Inventory::getSkuCode, Inventory::getQuantity));

		return skuCodes.stream()
				.collect(Collectors.toMap(
						skuCode -> skuCode,
						skuCode -> skuCodeToQuantity.getOrDefault(skuCode, 0) > 0
				));
	}
	
}
