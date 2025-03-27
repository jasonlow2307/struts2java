package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.entity.Item;
import com.deloitte.struts2java.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/prices")
public class PriceController {

    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);
    
    private final ItemService itemService;

    @Autowired
    public PriceController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/list")
    public String listPrices(Model model) {
        logger.info("PriceController listPrices method called");
        try {
            List<Item> items = itemService.getAllItems();
            model.addAttribute("items", items);
            return "price/list";
        } catch (Exception e) {
            logger.error("Error in PriceController", e);
            return "error";
        }
    }
}