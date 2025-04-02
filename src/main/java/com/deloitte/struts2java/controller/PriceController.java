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

@RestController 
@RequestMapping("/prices") 
public class PriceController {

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);
    
    private final ItemService itemService;

    @Autowired
    public PriceController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
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