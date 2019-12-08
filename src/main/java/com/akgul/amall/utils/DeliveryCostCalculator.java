package com.akgul.amall.utils;

import com.akgul.amall.model.Category;
import com.akgul.amall.model.Product;
import com.akgul.amall.model.ShoppingCart;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Getter
@Setter
public class DeliveryCostCalculator {

    public DeliveryCostCalculator(BigDecimal costPerDelivery, BigDecimal costPerProduct, BigDecimal fixedCost) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    private BigDecimal costPerDelivery;
    private BigDecimal costPerProduct;
    private BigDecimal fixedCost;
    private long deliveryCount = 0;
    private long productCount = 0;

    public double calculateFor(ShoppingCart cart) {
        HashMap<Product, Long> items = cart.getItems();
        HashSet<Category> categories = new HashSet<>();

        for (Map.Entry<Product, Long> item : items.entrySet()) {
            Category productCategory = item.getKey().getCategory();
            categories.add(productCategory);
            productCount += item.getValue();
        }

        deliveryCount = categories.size();

        return costPerDelivery.multiply(BigDecimal.valueOf(deliveryCount))
                .add(costPerProduct.multiply(BigDecimal.valueOf(productCount)))
                .add(fixedCost).doubleValue();
    }
}
