package com.deloitte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationResponseDTO {
    
    private List<ItemCalculationResultDTO> items;
    private BigDecimal totalPrice;
    private boolean isEmpty;
}
