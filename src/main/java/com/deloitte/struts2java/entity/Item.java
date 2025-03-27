package com.deloitte.struts2java.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class Item {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private int  quantity;
    private BigDecimal  totalPrice;
}
