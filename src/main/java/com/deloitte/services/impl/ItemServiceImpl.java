package com.deloitte.services.impl;

import com.deloitte.dto.*;
import com.deloitte.exceptions.ItemNotFoundException;
import com.deloitte.models.Item;
import com.deloitte.repositories.ItemRepository;
import com.deloitte.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    
    private final ItemRepository itemRepository;
    
    @Override
    @Transactional(readOnly = true)
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public ItemDTO getItemById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        
        return mapToDTO(item);
    }
    
    @Override
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO) {
        Item item = mapToEntity(itemDTO);
        Item savedItem = itemRepository.save(item);
        return mapToDTO(savedItem);
    }
    
    @Override
    @Transactional
    public ItemDTO updateItem(Long id, ItemDTO itemDTO) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(id));
        
        // Update fields
        existingItem.setName(itemDTO.getName());
        existingItem.setDescription(itemDTO.getDescription());
        existingItem.setPrice(itemDTO.getPrice());
        
        Item updatedItem = itemRepository.save(existingItem);
        return mapToDTO(updatedItem);
    }
    
    @Override
    @Transactional
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new ItemNotFoundException(id);
        }
        
        itemRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CalculationResponseDTO calculatePrices(CalculationRequestDTO request) {
        // Get IDs of all items with quantity > 0
        List<Long> itemIds = request.getItems().stream()
                .filter(item -> item.getQuantity() > 0)
                .map(ItemQuantityDTO::getItemId)
                .collect(Collectors.toList());
        
        if (itemIds.isEmpty()) {
            return CalculationResponseDTO.builder()
                    .items(new ArrayList<>())
                    .totalPrice(BigDecimal.ZERO)
                    .isEmpty(true)
                    .build();
        }
        
        // Create a map of item ID to quantity
        Map<Long, Integer> quantityMap = new HashMap<>();
        request.getItems().forEach(item -> {
            if (item.getQuantity() > 0) {
                quantityMap.put(item.getItemId(), item.getQuantity());
            }
        });
        
        // Get all items from the database
        List<Item> items = itemRepository.findAllById(itemIds);
        
        // Calculate subtotals and total price
        List<ItemCalculationResultDTO> resultItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        
        for (Item item : items) {
            Integer quantity = quantityMap.get(item.getId());
            BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(quantity));
            
            resultItems.add(ItemCalculationResultDTO.builder()
                    .name(item.getName())
                    .price(item.getPrice())
                    .quantity(quantity)
                    .subtotal(subtotal)
                    .build());
            
            totalPrice = totalPrice.add(subtotal);
        }
        
        return CalculationResponseDTO.builder()
                .items(resultItems)
                .totalPrice(totalPrice)
                .isEmpty(false)
                .build();
    }
    
    // Helper methods to map between entity and DTO
    private ItemDTO mapToDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .build();
    }
    
    private Item mapToEntity(ItemDTO itemDTO) {
        return Item.builder()
                .id(itemDTO.getId())
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .price(itemDTO.getPrice())
                .build();
    }
}
