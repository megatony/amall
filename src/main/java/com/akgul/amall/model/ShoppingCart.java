package com.akgul.amall.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter
@Setter
public class ShoppingCart extends AmallObject {
    HashMap<Product, Long> items;
}
