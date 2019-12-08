package com.akgul.amall.model;

import com.akgul.amall.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ShoppingCart extends AmallObject {
    private HashMap<Product, Long> items = new HashMap<>();
    private Campaign cartCampaign;
    private Coupon cartCoupon;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private BigDecimal totalDiscount = BigDecimal.ZERO;
    private BigDecimal deliveryCost = BigDecimal.ZERO;

    public void addItem(Product product, long quantity) {
        if (quantity == 0 || product.getQuantity() < quantity) {
            return;
        }

        totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        items.put(product, quantity);
    }

    protected void applyDiscounts(Campaign discount1, Campaign discount2, Campaign discount3) {
        BigDecimal discount1Amount = getDiscountAmount(totalPrice, discount1);
        BigDecimal discount2Amount = getDiscountAmount(totalPrice, discount2);
        BigDecimal discount3Amount = getDiscountAmount(totalPrice, discount3);

        HashMap<BigDecimal, Campaign> amountWithCampaigns = new HashMap<>();

        amountWithCampaigns.put(discount1Amount, discount1);
        amountWithCampaigns.put(discount2Amount, discount2);
        amountWithCampaigns.put(discount3Amount, discount3);

        Map.Entry<BigDecimal, Campaign> selected = amountWithCampaigns.entrySet()
                .stream().sorted((t0, t1) -> t1.getKey().compareTo(t0.getKey())).findFirst().get();

        if (!(selected.getKey().doubleValue() > 0)) {
            return;
        }

        cartCampaign = selected.getValue();
        totalDiscount = selected.getKey();
    }

    protected void applyCoupon(Coupon coupon) {}

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

    protected BigDecimal getDiscountAmount(BigDecimal totalPrice, Campaign discount) {
        if (discount.getDiscountType().equals(DiscountType.RATE)) {
            return totalPrice.multiply(discount.getDiscountAmount().divide(BigDecimal.valueOf(100)));
        }
        return discount.getDiscountAmount();
    }
}
