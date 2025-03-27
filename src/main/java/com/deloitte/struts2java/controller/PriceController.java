package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.entity.Item;
import com.deloitte.struts2java.service.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/prices")
@Tag(name = "Price Management", description = "APIs for listing and managing item prices")
public class PriceController {

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    private final ItemService itemService;

    @Autowired
    public PriceController(ItemService itemService) {
        this.itemService = itemService;
    }

    @Operation(summary = "List all item prices", description = "Displays a page with all available items and their prices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully displayed price list page"),
            @ApiResponse(responseCode = "500", description = "Server error occurred while fetching prices")
    })
    @GetMapping("/list")
    public ResponseEntity<?> listPrices(Model model) {
        logger.info("PriceController listPrices method called");
        try {
            List<Item> items = itemService.getAllItems();
            model.addAttribute("items", items);
            return ResponseEntity.ok().body(items);
        } catch (Exception e) {
            logger.error("Error in PriceController", e);
            return ResponseEntity.badRequest().body("Error fetching prices: " + e.getMessage());
        }
    }
}