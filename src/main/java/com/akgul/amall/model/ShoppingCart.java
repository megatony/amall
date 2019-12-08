package com.akgul.amall.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class ShoppingCart extends AmallObject {
    private HashMap<Product, Long> items = new HashMap<>();

    protected void addItem(Product product, long quantity) {

    }
}
