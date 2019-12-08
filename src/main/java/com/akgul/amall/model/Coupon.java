package com.akgul.amall.model;

import com.akgul.amall.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Coupon extends AmallObject {
    public Coupon(BigDecimal couponAmount, BigDecimal minimumPurchaseAmount, DiscountType discountType) {
        this.couponAmount = couponAmount;
        this.minimumPurchaseAmount = minimumPurchaseAmount;
        this.discountType = discountType;
    }
    private BigDecimal couponAmount;
    private BigDecimal minimumPurchaseAmount;
    private DiscountType discountType;
}
