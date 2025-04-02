package com.deloitte.struts2java.service;

import java.util.List;

import com.deloitte.struts2java.dto.ItemDTO;
import com.deloitte.struts2java.model.Item;
import com.deloitte.struts2java.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Item createItem(ItemDTO itemDTO) {
        // Create a new item with only the fields provided in the original implementation
        Item item = new Item();
        item.setName(itemDTO.getName());
        item.setDescription(itemDTO.getDescription());
        item.setPrice(itemDTO.getPrice());
        // Other fields are left at their default values

        return itemRepository.save(item);
    }

    @Transactional
    public Item updateItem(Item item) {
        return itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(int itemId) {
        return itemRepository.findById(itemId).orElse(null);
    }

    @Transactional
    public boolean deleteItemById(int itemId) {
        if (itemRepository.existsById(itemId)) {
            itemRepository.deleteById(itemId);
            return true;
        }
        return false;
    }
}