package com.akgul.amall.utils;

import com.akgul.amall.model.ShoppingCart;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

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

    protected double calculateFor(ShoppingCart cart) {
        return 0;
    }
}
