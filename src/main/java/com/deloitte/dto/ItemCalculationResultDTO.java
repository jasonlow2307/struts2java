package com.deloitte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCalculationResultDTO {
    
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
