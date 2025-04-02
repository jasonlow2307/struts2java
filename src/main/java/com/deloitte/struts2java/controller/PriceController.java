package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.model.Item;
import com.deloitte.struts2java.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController 
@RequestMapping("/api/prices")
@Tag(name = "Price Management", description = "APIs for retrieving and managing prices")
public class PriceController {

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);
    
    private final ItemService itemService;

    @Autowired
    public PriceController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @Operation(summary = "Get all items with prices", description = "Retrieves a list of all items with their current prices")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved price list",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Item.class))),
        @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    public ResponseEntity<List<Item>> listPrices() {
        logger.info("PriceController listPrices method called");
        
        try {
            List<Item> items = itemService.getAllItems();
            return ResponseEntity.ok(items); // Return items directly with 200 OK status
        } catch (Exception e) {
            logger.error("Error retrieving price list", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}