package com.deloitte.struts2java.controller;

import com.deloitte.struts2java.entity.Item;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/prices")
public class PriceSummaryController {

    private static final Logger logger = LoggerFactory.getLogger(PriceSummaryController.class);

    @PostMapping("/summary")
    public String processPriceSummary(
            @RequestParam(value = "itemId", required = false) String[] itemIds,
            @RequestParam(value = "quantity", required = false) String[] quantities,
            @RequestParam(value = "price", required = false) String[] prices,
            @RequestParam(value = "name", required = false) String[] names,
            HttpSession session) {

        logger.info("Processing price summary");
        logger.debug("ItemIds: {}", Arrays.toString(itemIds));
        logger.debug("Quantities: {}", Arrays.toString(quantities));
        logger.debug("Prices: {}", Arrays.toString(prices));
        logger.debug("Names: {}", Arrays.toString(names));

        try {
            // Validate input
            if (itemIds == null || quantities == null || prices == null || names == null
                    || itemIds.length != quantities.length || itemIds.length != prices.length) {
                return "error";
            }

            BigDecimal totalPrice = BigDecimal.ZERO;
            Map<Item, Integer> cartItems = new HashMap<>();

            for (int i = 0; i < itemIds.length; i++) {
                int itemId = Integer.parseInt(itemIds[i]);
                int quantity = Integer.parseInt(quantities[i]);
                BigDecimal price = new BigDecimal(prices[i]);
                String name = names[i];

                if (quantity > 0) {
                    Item item = new Item();
                    item.setId(itemId);
                    item.setName(name);
                    item.setPrice(price);

                    totalPrice = totalPrice.add(price.multiply(BigDecimal.valueOf(quantity)));
                    cartItems.put(item, quantity);
                }
            }

            session.setAttribute("totalPrice", totalPrice);
            session.setAttribute("cartItems", cartItems);

            logger.info("Total price: {}", totalPrice);
            logger.debug("Cart items: {}", cartItems);

            return "price/summary";

        } catch (Exception e) {
            logger.error("Error processing price summary", e);
            return "error";
        }
    }
}