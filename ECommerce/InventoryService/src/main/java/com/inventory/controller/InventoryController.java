package com.inventory.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.dto.InventoryRequestDto;
import com.inventory.dto.InventoryResponseDto;
import com.inventory.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("/inventories")
    @ResponseStatus(HttpStatus.CREATED)
    public String createInventory(@RequestBody InventoryRequestDto requestDto) {
         inventoryService.createInventory(requestDto);
         return "Inventory created successfully";
    }

    @GetMapping("/inventories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponseDto getInventoryById(@PathVariable("id") String id) {
        return inventoryService.getInventoryById(id);
    }

    @GetMapping("/inventories")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponseDto> getAllInventories() {
        return inventoryService.getAllInventories();
    }
    
    @GetMapping("/inventories/availableitems")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Boolean> getAvaliableItems(@RequestParam List<String> skuCode) {
      /*  try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Simulating delay for testing purposes*/
    	return inventoryService.getAvailabilityBySkuCodes(skuCode);
    }

    @PutMapping("/inventories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateInventory(@PathVariable("id") String id,
                                                @RequestBody InventoryRequestDto requestDto) {
         inventoryService.updateInventory(id, requestDto);
         return "Inventory updated successfully";
    }

    @DeleteMapping("/inventories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable("id") String id) {
        inventoryService.deleteInventory(id);
    }
}
