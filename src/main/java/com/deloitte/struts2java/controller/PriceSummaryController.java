package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.model.Item;
import com.deloitte.struts2java.dto.CartRequestDTO;
import com.deloitte.struts2java.dto.CartResponseDTO;
import com.deloitte.struts2java.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/prices")
@SessionAttributes({"totalPrice", "cartItems"})
@Tag(name = "Price Summary", description = "APIs for calculating price summaries and shopping cart operations")
public class PriceSummaryController {

    private static final Logger logger = LoggerFactory.getLogger(PriceSummaryController.class);

    private final ItemService itemService;

    @Autowired
    public PriceSummaryController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/summary")
    @Operation(
            summary = "Calculate price summary",
            description = "Calculates the total price and summarizes items in the cart using item IDs and quantities, fetching prices from database"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully calculated price summary",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CartResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data or item not found",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content
            )
    })
    public ResponseEntity<CartResponseDTO> calculateSummary(
            @Parameter(
                    description = "Cart items with IDs and quantities only",
                    required = true,
                    schema = @Schema(implementation = CartRequestDTO.class)
            )
            @RequestBody CartRequestDTO request) {
        logger.info("Processing price summary request: {}", request);

        try {
            BigDecimal totalPrice = BigDecimal.ZERO;
            Map<Item, Integer> cartItems = new HashMap<>();

            for (CartRequestDTO.CartItem cartItem : request.getItems()) {
                int itemId = cartItem.getItemId();
                int quantity = cartItem.getQuantity();

                if (quantity <= 0) {
                    continue;
                }

                // Fetch item details from database
                Item item = itemService.getItemById(itemId);

                // Check if item exists
                if (item == null) {
                    logger.warn("Item with ID {} not found", itemId);
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(new CartResponseDTO("Item not found with ID: " + itemId));
                }

                BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(quantity));
                totalPrice = totalPrice.add(itemTotal);
                cartItems.put(item, quantity);
            }

            // Create response
            CartResponseDTO response = new CartResponseDTO();
            response.setCartItems(cartItems);
            response.setTotalPrice(totalPrice);
            response.setMessage("Price summary calculated successfully");

            logger.info("Price summary calculated, total: {}", totalPrice);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing price summary", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CartResponseDTO("Error calculating price summary: " + e.getMessage()));
        }
    }
}