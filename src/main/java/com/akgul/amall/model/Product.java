package com.akgul.amall.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Product extends AmallObject {

    public Product(String name, BigDecimal price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    private String name;
    private BigDecimal price;
    private Category category;
    private long amount;
}
