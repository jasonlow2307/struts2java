package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.dto.ItemFormDto;
import com.deloitte.struts2java.entity.Item;
import com.deloitte.struts2java.service.ItemService;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);

    @PostMapping("")
    public ResponseEntity createItem(@RequestBody ItemFormDto itemForm, HttpSession session){
        try{
            Item savedItem = itemService.createItem(itemForm);
            return ResponseEntity.ok(savedItem);
        }catch (Exception e) {
            logger.error("Error in PriceController", e);
            return ResponseEntity.badRequest().body("Error creating Item: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity updateItem(@RequestBody ItemFormDto itemForm){
        try{
            Item updatedItem = itemService.updateItem(itemForm);
            return ResponseEntity.ok(updatedItem);
        }catch (Exception e) {
            logger.error("Error in PriceController", e);
            return ResponseEntity.badRequest().body("Error updating Item: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") int ItemId){
        try{
            itemService.deleteItemById(ItemId);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            logger.error("Error in PriceController", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/itemListing")
    public ResponseEntity itemListing(){
        try{
            List<Item> itemList =  itemService.getAllItems();
            return ResponseEntity.ok(itemList);
        }catch (Exception e) {
            logger.error("Error in PriceController", e);
            return ResponseEntity.badRequest().body("Error getting ItemList: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity viewItem(@PathVariable("id") int ItemId){
        try{
            Item item =  itemService.getItemById(ItemId);
            return ResponseEntity.ok(item);
        }catch (Exception e) {
            logger.error("Error in PriceController", e);
            return ResponseEntity.badRequest().body("Error getting ItemList: " + e.getMessage());
        }
    }

}
