package com.deloitte.struts2java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Request object containing cart items for price calculation")
@Getter
@Setter
public class CartRequestDTO {
    
    @Schema(description = "List of items in the cart with their quantities and prices")
    private List<CartItem> items;
    
    @Schema(description = "Individual item in the shopping cart")
    @Getter
    @Setter
    public static class CartItem {
        
        @Schema(description = "ID of the item", example = "1")
        private int itemId;
        
        @Schema(description = "Quantity of the item", example = "2")
        private int quantity;


    }
}