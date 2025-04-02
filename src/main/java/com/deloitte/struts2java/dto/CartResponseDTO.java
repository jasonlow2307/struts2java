package com.deloitte.struts2java.dto;

import com.deloitte.struts2java.model.Item;
import java.math.BigDecimal;
import java.util.Map;

public class CartResponseDTO {
    private Map<Item, Integer> cartItems;
    private BigDecimal totalPrice;
    
    public Map<Item, Integer> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(Map<Item, Integer> cartItems) {
        this.cartItems = cartItems;
    }
    
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}