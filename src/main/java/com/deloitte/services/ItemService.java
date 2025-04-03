package com.deloitte.services;

import com.deloitte.dto.CalculationRequestDTO;
import com.deloitte.dto.CalculationResponseDTO;
import com.deloitte.dto.ItemDTO;

import java.util.List;

public interface ItemService {
    
    List<ItemDTO> getAllItems();
    
    ItemDTO getItemById(Long id);
    
    ItemDTO createItem(ItemDTO itemDTO);
    
    ItemDTO updateItem(Long id, ItemDTO itemDTO);
    
    void deleteItem(Long id);
    
    CalculationResponseDTO calculatePrices(CalculationRequestDTO request);
}
