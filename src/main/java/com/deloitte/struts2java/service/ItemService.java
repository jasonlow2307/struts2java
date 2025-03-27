package com.deloitte.struts2java.service;

import com.deloitte.struts2java.dto.ItemFormDto;
import com.deloitte.struts2java.entity.Item;
import com.deloitte.struts2java.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    // Convert DTO to Entity
    private Item mapToEntity(ItemFormDto itemDTO) {
        return Item.builder()
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .price(itemDTO.getPrice())
                .quantity(0)
                .totalPrice(new BigDecimal("0.00"))
                .build();
    }


    @Transactional
    public Item createItem(ItemFormDto item){
        return itemRepository.save(mapToEntity(item));
    }

    @Transactional
    public Item updateItem(ItemFormDto itemForm){
        Item originalItem = getItemById(itemForm.getId());
        Item item = mapToEntity(itemForm);

        originalItem.setName(item.getName());
        originalItem.setDescription(item.getDescription());
        originalItem.setPrice(item.getPrice());
        itemRepository.save(originalItem);
        return originalItem;
    }

    public Item getItemById(int itemID){
        return itemRepository.findById(itemID)
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + itemID));
    }

    public List<Item> getAllItems(){
        List<Item> items = itemRepository.findAll();
        return items;
    }

    @Transactional
    public void deleteItemById(int itemId){
        Item item = getItemById(itemId);
        if (item != null) {
            itemRepository.delete(item);
            System.out.println("Item deleted successfully!");
        } else {
            System.out.println("Item not found!");
        }
    }
}
