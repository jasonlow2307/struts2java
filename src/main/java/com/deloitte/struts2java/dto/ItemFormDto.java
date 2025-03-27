package com.deloitte.struts2java.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemFormDto {
    private int id;
    private String name;
    private String description;
    private BigDecimal price;
}
