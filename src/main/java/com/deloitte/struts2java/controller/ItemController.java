package com.deloitte.struts2java.controller;

import java.util.List;

import com.deloitte.struts2java.model.Item;
import com.deloitte.struts2java.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/items")
@SessionAttributes("selectedItem")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ModelAttribute("selectedItem")
    public Item selectedItem() {
        return new Item();
    }

    @GetMapping("/list")
    public String listItems(Model model) {
        try {
            List<Item> items = itemService.getAllItems();
            model.addAttribute("items", items);
            return "item/itemListing";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect";
        }
    }

    @GetMapping("/view/{id}")
    public String viewItem(@PathVariable("id") int itemId, Model model) {
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return "redirect";
        }

        model.addAttribute("item", item);
        model.addAttribute("selectedItem", item);
        return "item/itemDetails";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("item", new Item());
        return "item/createItem";
    }

    @PostMapping("/create")
    public String createItem(@ModelAttribute Item item, Model model, RedirectAttributes redirectAttributes) {
        try {
            itemService.createItem(item);
            model.addAttribute("item", item);
            model.addAttribute("selectedItem", item);
            return "item/itemDetails";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int itemId, Model model) {
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            return "redirect";
        }

        model.addAttribute("item", item);
        return "item/editItem";
    }

    @PostMapping("/update")
    public String updateItem(@ModelAttribute Item item, Model model) {
        try {
            itemService.updateItem(item);
            model.addAttribute("item", item);
            model.addAttribute("selectedItem", item);
            return "item/itemDetails";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable("id") int itemId, Model model) {
        try {
            itemService.deleteItemById(itemId);
            List<Item> items = itemService.getAllItems();
            model.addAttribute("items", items);
            return "item/itemListing";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect";
        }
    }
}