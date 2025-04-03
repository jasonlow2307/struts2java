package com.deloitte.controllers;

import com.deloitte.dto.*;
import com.deloitte.services.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
@Tag(name = "Item Management", description = "APIs for managing items and calculating prices")
public class ItemController {
    
    private final ItemService itemService;
    
    @GetMapping
    @Operation(summary = "Get all items", description = "Returns a list of all available items")
    public ResponseEntity<ApiResponse<List<ItemDTO>>> getAllItems() {
        List<ItemDTO> items = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Returns a specific item by its ID")
    public ResponseEntity<ApiResponse<ItemDTO>> getItemById(@PathVariable Long id) {
        ItemDTO item = itemService.getItemById(id);
        return ResponseEntity.ok(ApiResponse.success(item));
    }
    
    @PostMapping
    @Operation(summary = "Create a new item", description = "Creates a new item with the provided details")
    public ResponseEntity<ApiResponse<ItemDTO>> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        ItemDTO createdItem = itemService.createItem(itemDTO);
        return new ResponseEntity<>(ApiResponse.success(createdItem), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an item", description = "Updates an existing item with the provided details")
    public ResponseEntity<ApiResponse<ItemDTO>> updateItem(
            @PathVariable Long id, 
            @Valid @RequestBody ItemDTO itemDTO) {
        ItemDTO updatedItem = itemService.updateItem(id, itemDTO);
        return ResponseEntity.ok(ApiResponse.success(updatedItem));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item", description = "Deletes an item by its ID")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long id) {
        itemService.deleteItem(id);
        return ResponseEntity.ok(ApiResponse.<Void>success(null));
    }
    
    @PostMapping("/calculate")
    @Operation(summary = "Calculate prices", description = "Calculates subtotals and total price based on selected items and quantities")
    public ResponseEntity<ApiResponse<CalculationResponseDTO>> calculatePrices(
            @Valid @RequestBody CalculationRequestDTO request) {
        CalculationResponseDTO response = itemService.calculatePrices(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
