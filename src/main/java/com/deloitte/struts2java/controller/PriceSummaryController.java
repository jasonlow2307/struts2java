package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.model.Item;
import com.deloitte.struts2java.dto.CartRequestDTO;
import com.deloitte.struts2java.dto.CartResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/prices")
@SessionAttributes({"totalPrice", "cartItems"})
public class PriceSummaryController {

    private static final Logger logger = LoggerFactory.getLogger(PriceSummaryController.class);

    @PostMapping("/summary")
    public ResponseEntity<CartResponseDTO> calculateSummary(@RequestBody CartRequestDTO request) {
        logger.info("Processing price summary request: {}", request);
        
        try {
            // Extract data from the request
            BigDecimal totalPrice = BigDecimal.ZERO;
            Map<Item, Integer> cartItems = new HashMap<>();

            for (CartRequestDTO.CartItem cartItem : request.getItems()) {
                int quantity = cartItem.getQuantity();
                
                if (quantity > 0) {
                    Item item = new Item();
                    item.setId(cartItem.getItemId());
                    item.setName(cartItem.getName());
                    item.setPrice(cartItem.getPrice());

                    BigDecimal itemTotal = cartItem.getPrice().multiply(BigDecimal.valueOf(quantity));
                    totalPrice = totalPrice.add(itemTotal);
                    cartItems.put(item, quantity);
                }
            }

            // Create response
            CartResponseDTO response = new CartResponseDTO();
            response.setCartItems(cartItems);
            response.setTotalPrice(totalPrice);
            
            logger.info("Price summary calculated, total: {}", totalPrice);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error processing price summary", e);
            return ResponseEntity.badRequest().build();
        }
    }
}