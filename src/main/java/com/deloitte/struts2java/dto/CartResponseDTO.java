package com.deloitte.struts2java.dto;

import com.deloitte.struts2java.model.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Response containing price summary information")
@NoArgsConstructor
@Getter
@Setter
public class CartResponseDTO {
    
    @Schema(description = "Map of items in the cart with their quantities")
    private Map<Item, Integer> cartItems;
    
    @Schema(description = "Total price of all items in the cart", example = "119.98")
    private BigDecimal totalPrice;

    private String message;

    public CartResponseDTO(String message) {
        this.message = message;
    }
}