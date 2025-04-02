package com.deloitte.struts2java.controller;

import java.util.List;

import com.deloitte.struts2java.Dto.ApiResponse;
import com.deloitte.struts2java.Dto.ItemDTO;
import com.deloitte.struts2java.model.Item;
import com.deloitte.struts2java.service.ItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Item>>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return ResponseEntity.ok(ApiResponse.success(items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> getItemById(@PathVariable int id) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Item not found with id: " + id));
        }
        return ResponseEntity.ok(ApiResponse.success(item));
    }

    @GetMapping("/create")
    public ResponseEntity<ApiResponse<String>> showCreateForm() {
        // This endpoint simulates forwarding to a create form in the original Struts implementation
        return ResponseEntity.ok(ApiResponse.success("Ready to create item", null));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Item>> createItem(
            @RequestBody ItemDTO itemDTO,
            HttpSession session) {
        try {
            System.out.println("Creating item: " + itemDTO.getName());

            // Only include name, description, and price as in the original implementation
            Item createdItem = itemService.createItem(itemDTO);

            // Store in session as the original implementation did
            session.setAttribute("selectedItem", createdItem);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Item created successfully", createdItem));
        } catch (Exception e) {
            System.out.println("Error creating item: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create item: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Item>> updateItem(@PathVariable int id, @RequestBody Item item) {
        try {
            Item existingItem = itemService.getItemById(id);

            if (existingItem == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Item not found with id: " + id));
            }

            // Set ID to ensure we're updating the correct record
            item.setId(id);
            Item updatedItem = itemService.updateItem(item);

            return ResponseEntity.ok(ApiResponse.success("Item updated successfully", updatedItem));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to update item: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable int id) {
        try {
            boolean deleted = itemService.deleteItemById(id);

            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Item not found with id: " + id));
            }

            return ResponseEntity.ok(ApiResponse.success("Item deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete item: " + e.getMessage()));
        }
    }
}