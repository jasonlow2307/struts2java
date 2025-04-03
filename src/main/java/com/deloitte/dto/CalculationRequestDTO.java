package com.deloitte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationRequestDTO {
    
    @NotEmpty(message = "Item quantities cannot be empty")
    @Valid
    private List<ItemQuantityDTO> items;
}
