package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.dto.ItemFormDto;
import com.deloitte.struts2java.entity.Item;
import com.deloitte.struts2java.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
@Tag(name = "Item Controller", description = "API for managing items")
public class ItemController {
    @Autowired
    private ItemService itemService;

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    @Operation(summary = "Create a new item", description = "Creates a new item based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item created successfully",
                    content = @Content(schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("")
    public ResponseEntity createItem(@RequestBody ItemFormDto itemForm, HttpSession session){
        try{
            Item savedItem = itemService.createItem(itemForm);
            return ResponseEntity.ok(savedItem);
        }catch (Exception e) {
            logger.error("Error in ItemController", e);
            return ResponseEntity.badRequest().body("Error creating Item: " + e.getMessage());
        }
    }

    @Operation(summary = "Update an item", description = "Updates an existing item based on the provided data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully",
                    content = @Content(schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity updateItem(@RequestBody ItemFormDto itemForm){
        try{
            Item updatedItem = itemService.updateItem(itemForm);
            return ResponseEntity.ok(updatedItem);
        }catch (Exception e) {
            logger.error("Error in ItemController", e);
            return ResponseEntity.badRequest().body("Error updating Item: " + e.getMessage());
        }
    }

    @Operation(summary = "Delete an item", description = "Deletes an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID or error during deletion")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") int ItemId){
        try{
            itemService.deleteItemById(ItemId);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            logger.error("Error in ItemController", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get all items", description = "Retrieves a list of all items")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Error retrieving items")
    })
    @GetMapping("/itemListing")
    public ResponseEntity itemListing(){
        try{
            List<Item> itemList = itemService.getAllItems();
            return ResponseEntity.ok(itemList);
        }catch (Exception e) {
            logger.error("Error in ItemController", e);
            return ResponseEntity.badRequest().body("Error getting ItemList: " + e.getMessage());
        }
    }

    @Operation(summary = "Get item by ID", description = "Retrieves an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item retrieved successfully",
                    content = @Content(schema = @Schema(implementation = Item.class))),
            @ApiResponse(responseCode = "400", description = "Invalid ID or error retrieving item")
    })
    @GetMapping("/{id}")
    public ResponseEntity viewItem(@PathVariable("id") int ItemId){
        try{
            Item item = itemService.getItemById(ItemId);
            return ResponseEntity.ok(item);
        }catch (Exception e) {
            logger.error("Error in ItemController", e);
            return ResponseEntity.badRequest().body("Error getting Item: " + e.getMessage());
        }
    }
}