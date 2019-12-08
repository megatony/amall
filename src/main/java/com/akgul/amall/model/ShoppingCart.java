package com.akgul.amall.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;

@Getter
@Setter
public class ShoppingCart extends AmallObject {
    private HashMap<Product, Long> items = new HashMap<>();
    private Campaign cartCampaign;
    private Coupon cartCoupon;
    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;
    private BigDecimal deliveryCost;

    protected void addItem(Product product, long quantity) {
        if (quantity == 0 || product.getQuantity() < quantity) {
            return;
        }

        items.put(product, quantity);
    }

    protected void applyDiscounts(Campaign discount1, Campaign discount2, Campaign discount3) {

    }

    protected double getTotalAmountAfterDiscounts() {
        return 0;
    }

    protected double getCouponDiscount() {
        return 0;
    }

    protected double getCampaignDiscount() {
        return 0;
    }

    protected double getDeliveryCost() {
        return 0;
    }

    protected String print() {
        return "";
    }
}
