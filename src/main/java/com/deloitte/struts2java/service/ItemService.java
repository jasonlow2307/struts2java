package com.deloitte.struts2java.service;

import java.util.List;
import java.util.Optional;

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
    public void createItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(int itemId) {
        Optional<Item> itemOptional = itemRepository.findById(itemId);
        return itemOptional.orElse(null);
    }

    @Transactional
    public void deleteItemById(int itemId) {
        if (itemRepository.existsById(itemId)) {
            itemRepository.deleteById(itemId);
            System.out.println("Item deleted successfully!");
        } else {
            System.out.println("Item not found!");
        }
    }
}