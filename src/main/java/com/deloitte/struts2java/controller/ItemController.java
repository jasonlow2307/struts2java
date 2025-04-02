package com.deloitte.struts2java.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deloitte.struts2java.dto.ItemDTO;
import com.deloitte.struts2java.model.Item;
import com.deloitte.struts2java.service.ItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/items")
@Tag(name = "Item Management", description = "APIs for managing items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @Operation(summary = "Get all items", description = "Retrieves a list of all items in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved all items"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<com.deloitte.struts2java.dto.ApiResponse<List<Item>>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(com.deloitte.struts2java.dto.ApiResponse.success(items));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get item by ID", description = "Retrieves an item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved the item"),
        @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ResponseEntity<com.deloitte.struts2java.dto.ApiResponse<Item>> getItemById(
            @Parameter(description = "ID of the item to retrieve") @PathVariable int id) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(com.deloitte.struts2java.dto.ApiResponse.error("Item not found with id: " + id));
        }
        return ResponseEntity.ok(com.deloitte.struts2java.dto.ApiResponse.success(item));
    }

    @GetMapping("/create")
    @Operation(summary = "Show create form", description = "Simulates forwarding to a create form")
    public ResponseEntity<com.deloitte.struts2java.dto.ApiResponse<String>> showCreateForm() {
        return ResponseEntity.ok(com.deloitte.struts2java.dto.ApiResponse.success("Ready to create item", null));
    }

    @PostMapping
    @Operation(summary = "Create a new item", description = "Creates a new item with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item successfully created"),
        @ApiResponse(responseCode = "500", description = "Failed to create the item")
    })
    public ResponseEntity<com.deloitte.struts2java.dto.ApiResponse<Item>> createItem(
            @RequestBody ItemDTO itemDTO,
            HttpSession session) {
        try {
            System.out.println("Creating item: " + itemDTO.getName());

            Item createdItem = itemService.createItem(itemDTO);
            session.setAttribute("selectedItem", createdItem);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(com.deloitte.struts2java.dto.ApiResponse.success("Item created successfully", createdItem));
        } catch (Exception e) {
            System.out.println("Error creating item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(com.deloitte.struts2java.dto.ApiResponse.error("Failed to create item: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an item", description = "Updates an existing item with the provided data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item successfully updated"),
        @ApiResponse(responseCode = "404", description = "Item not found"),
        @ApiResponse(responseCode = "500", description = "Failed to update the item")
    })
    public ResponseEntity<com.deloitte.struts2java.dto.ApiResponse<Item>> updateItem(
            @Parameter(description = "ID of the item to update") @PathVariable int id, 
            @RequestBody Item item) {
        try {
            Item existingItem = itemService.getItemById(id);

            if (existingItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.deloitte.struts2java.dto.ApiResponse.error("Item not found with id: " + id));
            }

            item.setId(id);
            Item updatedItem = itemService.updateItem(item);

            return ResponseEntity.ok(com.deloitte.struts2java.dto.ApiResponse.success("Item updated successfully", updatedItem));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(com.deloitte.struts2java.dto.ApiResponse.error("Failed to update item: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an item", description = "Deletes an item by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Item not found"),
        @ApiResponse(responseCode = "500", description = "Failed to delete the item")
    })
    public ResponseEntity<com.deloitte.struts2java.dto.ApiResponse<Void>> deleteItem(
            @Parameter(description = "ID of the item to delete") @PathVariable int id) {
        try {
            boolean deleted = itemService.deleteItemById(id);

            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(com.deloitte.struts2java.dto.ApiResponse.error("Item not found with id: " + id));
            }

            return ResponseEntity.ok(com.deloitte.struts2java.dto.ApiResponse.success("Item deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(com.deloitte.struts2java.dto.ApiResponse.error("Failed to delete item: " + e.getMessage()));
        }
    }
}