package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.entity.Item;
import com.deloitte.struts2java.repository.ItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/prices")
@Tag(name = "Price Summary", description = "API for calculating price summaries based on selected items")
public class PriceSummaryController {

    private static final Logger logger = LoggerFactory.getLogger(PriceSummaryController.class);
    private final ItemRepository itemRepository;

    public PriceSummaryController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Operation(summary = "Calculate price summary",
            description = "Processes selected item IDs and quantities and calculates the total price using database values.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully calculated price summary"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Error occurred during price calculation")
    })
    @PostMapping("/summary")
    public ResponseEntity<?> processPriceSummary(
            @Parameter(description = "Array of selected item IDs")
            @RequestParam(value = "itemId", required = false) String[] itemIds,

            @Parameter(description = "Array of quantities for each selected item")
            @RequestParam(value = "quantity", required = false) String[] quantities,

            HttpSession session) {

        logger.info("Processing price summary");

        if (itemIds == null || quantities == null || itemIds.length != quantities.length) {
            return ResponseEntity.badRequest().body("Invalid input data");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        Map<String, Integer> cartItems = new HashMap<>();

        try {
            for (int i = 0; i < itemIds.length; i++) {
                int itemId = Integer.parseInt(itemIds[i]);
                int quantity = Integer.parseInt(quantities[i]);

                Optional<Item> optionalItem = itemRepository.findById(itemId);

                if (optionalItem.isPresent() && quantity > 0) {
                    Item item = optionalItem.get();
                    BigDecimal price = item.getPrice();
                    String name = item.getName();

                    totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(quantity)));
                    cartItems.put(name, quantity);
                } else {
                    logger.warn("Item with ID {} not found or invalid quantity", itemId);
                }
            }

            session.setAttribute("totalPrice", totalPrice);
            session.setAttribute("cartItems", cartItems);

            logger.info("Total price: {}", totalPrice);
            logger.debug("Cart items: {}", cartItems);

            return ResponseEntity.ok().body(Map.of(
                    "totalPrice", totalPrice,
                    "cartItems", cartItems
            ));

        } catch (Exception e) {
            logger.error("Error processing price summary", e);
            return ResponseEntity.badRequest().body("Error processing price summary: " + e.getMessage());
        }
    }
}
