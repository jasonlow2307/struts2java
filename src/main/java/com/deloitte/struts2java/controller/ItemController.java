package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService){
        this.itemService = itemService;
    }

    //@GetMapping("/itemListing")

    //@GetMapping("/viewItems")

    //@GetMapping("/createItem")

    //@GetMapping("/editItems")

    //@GetMapping("/deleteItems")

}
