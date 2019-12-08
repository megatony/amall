package com.akgul.amall.model;

import com.akgul.amall.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Campaign extends AmallObject {

    public Campaign(Category category, BigDecimal discountAmount, long minimumQuantity, DiscountType discountType) {
        this.category = category;
        this.discountAmount = discountAmount;
        this.minimumQuantity = minimumQuantity;
        this.discountType = discountType;
    }

    private String name;
    private Category category;
    private BigDecimal discountAmount;
    private long minimumQuantity;
    private DiscountType discountType;
}
