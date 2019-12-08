package com.akgul.amall.model;

import com.akgul.amall.enums.DiscountType;
import com.akgul.amall.utils.DeliveryCostCalculator;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShoppingCart extends AmallObject {
    private static final BigDecimal COST_PER_DELIVERY = BigDecimal.valueOf(2);
    private static final BigDecimal COST_PER_PRODUCT = BigDecimal.valueOf(3);
    private static final BigDecimal FIXED_COST = BigDecimal.valueOf(2.99);

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

    protected void applyCoupon(Coupon coupon) {
        BigDecimal couponDiscountAmount;

        if (totalPrice.compareTo(coupon.getMinimumPurchaseAmount()) <= 0) {
            return;
        }

        if (coupon.getDiscountType().equals(DiscountType.RATE)) {
            couponDiscountAmount = totalPrice.multiply(coupon.getCouponAmount().divide(BigDecimal.valueOf(100)));
        } else {
            couponDiscountAmount = coupon.getCouponAmount();
        }

        if (!(couponDiscountAmount.doubleValue() > 0)) {
            return;
        }

        cartCoupon = coupon;
        totalDiscount = couponDiscountAmount;
    }

    protected double getTotalAmountAfterDiscounts() {
        return totalPrice.subtract(totalDiscount).doubleValue();
    }

    protected double getCouponDiscount() {
        if (cartCampaign != null || cartCoupon == null) {
            return 0;
        }
        return totalDiscount.doubleValue();
    }

    protected double getCampaignDiscount() {
        if (cartCampaign == null) {
            return 0;
        }
        return totalDiscount.doubleValue();
    }

    protected double getDeliveryCost() {
        DeliveryCostCalculator costCalculator = new DeliveryCostCalculator(COST_PER_DELIVERY, COST_PER_PRODUCT, FIXED_COST);
        deliveryCost = BigDecimal.valueOf(costCalculator.calculateFor(this));
        return deliveryCost.doubleValue();
    }

    protected String print() {
        StringBuilder builder = new StringBuilder();
        ArrayList<Category> categories = items.entrySet().stream().map(x->x.getKey().getCategory()).sorted(Comparator.comparingLong(x->x.getId())).collect(Collectors.toCollection(ArrayList::new));

        for (Category category : categories) {
            builder.append(category.getName()).append(" products\n");
            for (Map.Entry<Product, Long> item : items.entrySet()) {
                if (item.getKey().getCategory().equals(category)) {
                    Product product = item.getKey();
                    builder.append(product.getCategory().getName() + ", ");
                    builder.append(product.getName() + ", ");
                    builder.append(item.getValue() + ", ");
                    builder.append(product.getPrice() + ", ");

                    BigDecimal totalProductPrice = product.getPrice().multiply(BigDecimal.valueOf(item.getValue()));
                    builder.append(totalProductPrice + ", ");
                    builder.append(totalDiscount.divide(totalPrice).multiply(totalProductPrice) + "\n");
                }
            }
        }

        builder.append(getTotalAmountAfterDiscounts() + ", " + getDeliveryCost());

        return builder.toString();
    }

    protected BigDecimal getDiscountAmount(BigDecimal totalPrice, Campaign discount) {
        if (discount.getDiscountType().equals(DiscountType.RATE)) {
            return totalPrice.multiply(discount.getDiscountAmount().divide(BigDecimal.valueOf(100)));
        }
        return discount.getDiscountAmount();
    }
}
