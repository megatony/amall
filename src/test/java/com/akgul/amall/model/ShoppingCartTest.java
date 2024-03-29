package com.akgul.amall.model;


import com.akgul.amall.enums.DiscountType;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class ShoppingCartTest {

    @Test
    public void shouldAddItemToCartWhenProductQuantityIsValid() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category("TestCategory");
        Product product = new Product("TestProduct", BigDecimal.ONE, category);
        product.setQuantity(10);
        shoppingCart.addItem(product, 5);

        Assert.isTrue(shoppingCart.getItems().size() == 1, "Product should be added to cart.");
    }

    @Test
    public void shouldNotAddItemToCartWhenProductQuantityNotExist() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category("TestCategory");
        Product product = new Product("TestProduct", BigDecimal.ONE, category);
        product.setQuantity(0);
        shoppingCart.addItem(product, 5);

        Assert.isTrue(shoppingCart.getItems().size() == 0, "Product should not be added when product quantity is not valid");
    }

    @Test
    public void shouldNotAddItemToCartWhenAddedQuantityIsZero() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Category category = new Category("TestCategory");
        Product product = new Product("TestProduct", BigDecimal.ONE, category);
        product.setQuantity(10);
        shoppingCart.addItem(product, 0);

        Assert.isTrue(shoppingCart.getItems().size() == 0, "Product should not be added when added quantity is zero");
    }

    @Test
    public void shouldApplyTheMaximumAmountOfDiscountToTheCart() {
        Category category = new Category("TestCategory");

        Campaign biggestAmountCampaign = new Campaign(category, BigDecimal.TEN, 1, DiscountType.AMOUNT);
        Campaign campaign = new Campaign(category, BigDecimal.ONE, 1, DiscountType.AMOUNT);
        Campaign zeroCampaign = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.AMOUNT);

        Product product = new Product("TestProduct", BigDecimal.valueOf(100), category);
        product.setQuantity(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(biggestAmountCampaign, campaign, zeroCampaign);

        Assert.isTrue(shoppingCart.getCartCampaign().equals(biggestAmountCampaign), "Should select biggest amount campaign");
    }

    @Test
    public void shouldApplyTheMaximumAmountOfWhenDifferentDiscountTypesAvailable() {
        Category category = new Category("TestCategory");

        Campaign amountCampaign = new Campaign(category, BigDecimal.TEN, 1, DiscountType.AMOUNT);
        Campaign rateCampaign = new Campaign(category, BigDecimal.valueOf(25), 1, DiscountType.RATE);
        Campaign zeroCampaign = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.AMOUNT);

        Product product = new Product("TestProduct", BigDecimal.valueOf(100), category);
        product.setQuantity(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(amountCampaign, rateCampaign, zeroCampaign);

        Assert.isTrue(shoppingCart.getCartCampaign().equals(rateCampaign), "When cart total price 100, 25% discount has bigger amount than 10 amount");
    }

    @Test
    public void shouldNotApplyTheCampaignWhenAmountIsZero() {
        Category category = new Category("TestCategory");

        Campaign zeroCampaign1 = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.AMOUNT);
        Campaign zeroCampaign2 = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.RATE);
        Campaign zeroCampaign3 = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.AMOUNT);

        Product product = new Product("TestProduct", BigDecimal.valueOf(100), category);
        product.setQuantity(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);
        shoppingCart.applyDiscounts(zeroCampaign1, zeroCampaign2, zeroCampaign3);

        Assert.isNull(shoppingCart.getCartCampaign(), "Should not be applied when campaign values are zero.");
    }

    @Test
    public void shouldGetDiscountAmountWhenDiscountTypeIsRate() {
        Category category = new Category("TestCategory");
        Campaign rateCampaign = new Campaign(category, BigDecimal.valueOf(25), 1, DiscountType.RATE);

        ShoppingCart shoppingCart = new ShoppingCart();
        BigDecimal totalPrice = BigDecimal.valueOf(50);

        BigDecimal discountAmount = shoppingCart.getDiscountAmount(totalPrice, rateCampaign);
        Assert.isTrue(discountAmount.doubleValue() == 12.50, "Should get discount amount as amount not rate.");

    }

    @Test
    public void shouldNotApplyWhenCartTotalIsLessThanCouponMinimumAmount() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTotalPrice(BigDecimal.ONE);

        Coupon coupon = new Coupon(BigDecimal.ONE, BigDecimal.TEN, DiscountType.AMOUNT);
        shoppingCart.applyCoupon(coupon);
        Assert.isNull(shoppingCart.getCartCoupon(), "Should not apply coupon discount because of amount limit.");
    }

    @Test
    public void shouldApplyCouponToCartWhenCategoryDiscountIsNotAvailable() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartCampaign(null);

        Coupon coupon = new Coupon(BigDecimal.ONE, BigDecimal.TEN, DiscountType.AMOUNT);
        shoppingCart.setTotalPrice(BigDecimal.valueOf(100));
        shoppingCart.applyCoupon(coupon);

        Assert.isTrue(shoppingCart.getTotalDiscount().doubleValue() == 1, "After coupon discount applied, amount should be equal to coupon discount.");
    }

    @Test
    public void shouldApplyCouponDiscountWithAmountWhenDiscountTypeIsRate() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setCartCampaign(null);

        Coupon coupon = new Coupon(BigDecimal.TEN, BigDecimal.TEN, DiscountType.RATE);
        shoppingCart.setTotalPrice(BigDecimal.valueOf(50));
        shoppingCart.applyCoupon(coupon);

        Assert.isTrue(shoppingCart.getTotalDiscount().doubleValue() == 5, "After coupon discount applied, total discount should be amount not rate.");
    }

    @Test
    public void shouldGetTotalAmountAfterDiscountsWhenCampaignApplied() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTotalPrice(BigDecimal.valueOf(100));
        shoppingCart.setTotalProductQuantity(25);

        Category category = new Category("TestCategory");
        Campaign campaign1 = new Campaign(category, BigDecimal.TEN, 1, DiscountType.AMOUNT);
        Campaign campaign2 = new Campaign(category, BigDecimal.ONE, 1, DiscountType.AMOUNT);
        Campaign campaign3 = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.AMOUNT);
        shoppingCart.applyDiscounts(campaign1, campaign2, campaign3);

        Assert.isTrue(shoppingCart.getTotalAmountAfterDiscounts() == 90, "After biggest amount campaign applied, total amount should get from campaign1");
    }

    @Test
    public void shouldGetTotalAmountAfterDiscountsWhenCouponApplied() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTotalPrice(BigDecimal.valueOf(100));

        Coupon coupon = new Coupon(BigDecimal.TEN, BigDecimal.ZERO, DiscountType.AMOUNT);
        shoppingCart.applyCoupon(coupon);

        Assert.isTrue(shoppingCart.getTotalAmountAfterDiscounts() == 90, "After biggest amount campaign applied, total amount should get from campaign1");
    }

    @Test
    public void shouldGetCouponDiscountWhenItsAvailable() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTotalPrice(BigDecimal.valueOf(100));

        Coupon coupon = new Coupon(BigDecimal.TEN, BigDecimal.ZERO, DiscountType.AMOUNT);
        shoppingCart.applyCoupon(coupon);

        Assert.isTrue(shoppingCart.getCouponDiscount() == 10, "After coupon applied, coupon discount should be accessible from method.");
    }

    @Test
    public void shouldGetCampaignDiscount() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTotalPrice(BigDecimal.valueOf(100));
        shoppingCart.setTotalProductQuantity(25);

        Category category = new Category("TestCategory");
        Campaign campaign1 = new Campaign(category, BigDecimal.TEN, 1, DiscountType.AMOUNT);
        Campaign campaign2 = new Campaign(category, BigDecimal.ONE, 1, DiscountType.AMOUNT);
        Campaign campaign3 = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.AMOUNT);
        shoppingCart.applyDiscounts(campaign1, campaign2, campaign3);

        Assert.isTrue(shoppingCart.getCampaignDiscount() == 10, "After biggest amount campaign applied, campaign discount should be accessible.");
    }

    @Test
    public void shouldGetDeliveryCost() {
        Category firstCategory = new Category("FirstCategory");
        Product firstProduct = new Product("FirstProduct", BigDecimal.ONE, firstCategory);
        firstProduct.setQuantity(10);

        Category secondCategory = new Category("SecondCategory");
        Product secondProduct = new Product("SecondProduct", BigDecimal.ONE, secondCategory);
        secondProduct.setQuantity(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(firstProduct, 1);
        shoppingCart.addItem(secondProduct, 1);

        BigDecimal costPerDelivery = BigDecimal.valueOf(2.0);
        BigDecimal costPerProduct = BigDecimal.valueOf(3.0);
        BigDecimal fixedCost = BigDecimal.valueOf(2.99);
        double expectedDeliveryCost = (costPerDelivery.multiply(BigDecimal.valueOf(2))).add(costPerProduct.multiply(BigDecimal.valueOf(2))).add(fixedCost).doubleValue();

        Assert.isTrue(expectedDeliveryCost == shoppingCart.getDeliveryCost(), "Should get delivery cost.");
    }

    @Test
    public void shouldPrintWithFormatWhenCouponApplied() {
        Category firstCategory = new Category("FirstCategory");
        Product firstProduct = new Product("FirstProduct", BigDecimal.ONE, firstCategory);
        firstProduct.setQuantity(10);

        Category secondCategory = new Category("SecondCategory");
        Product secondProduct = new Product("SecondProduct", BigDecimal.ONE, secondCategory);
        secondProduct.setQuantity(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(firstProduct, 5);
        shoppingCart.addItem(secondProduct, 5);

        Coupon coupon = new Coupon(BigDecimal.TEN, BigDecimal.ZERO, DiscountType.AMOUNT);
        shoppingCart.applyCoupon(coupon);

        Assert.hasText(shoppingCart.print(), "FirstCategory products\n" +
                "FirstCategory, FirstProduct, 5, 1.00, 5.00, 0.10\n" +
                "SecondCategory products\n" +
                "SecondCategory, SecondProduct, 5, 1.00, 5.00, 0.10\n" +
                "9.00, 10.99");
    }

    @Test
    public void shouldPrintWithFormatWhenCampaignApplied() {
        Category firstCategory = new Category("FirstCategory");
        firstCategory.setId(1);
        Product firstProduct = new Product("FirstProduct", BigDecimal.ONE, firstCategory);
        firstProduct.setQuantity(10);

        Category secondCategory = new Category("SecondCategory");
        secondCategory.setId(2);
        Product secondProduct = new Product("SecondProduct", BigDecimal.ONE, secondCategory);
        secondProduct.setQuantity(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(firstProduct, 5);
        shoppingCart.addItem(secondProduct, 5);

        Category category = new Category("TestCategory");
        Campaign campaign1 = new Campaign(category, BigDecimal.ONE, 1, DiscountType.AMOUNT);
        Campaign campaign2 = new Campaign(category, BigDecimal.ONE, 1, DiscountType.AMOUNT);
        Campaign campaign3 = new Campaign(category, BigDecimal.ZERO, 1, DiscountType.AMOUNT);
        shoppingCart.applyDiscounts(campaign1, campaign2, campaign3);

        Assert.isTrue(shoppingCart.print().equals("FirstCategory products\n" +
                "FirstCategory, FirstProduct, 5, 1, 5, 0.5\n" +
                "SecondCategory products\n" +
                "SecondCategory, SecondProduct, 5, 1, 5, 0.5\n" +
                "9.0, 12.99"), "Should return exact string.");
    }

    @Test
    public void shouldNotApplyCampaignWhenMinimumQuantityIsNotValid() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setTotalPrice(BigDecimal.valueOf(100));
        shoppingCart.setTotalProductQuantity(1);
        Category category = new Category("TestCategory");
        Campaign campaign1 = new Campaign(category, BigDecimal.TEN, 2, DiscountType.AMOUNT);
        Campaign campaign2 = new Campaign(category, BigDecimal.ONE, 2, DiscountType.AMOUNT);
        Campaign campaign3 = new Campaign(category, BigDecimal.ZERO, 2, DiscountType.AMOUNT);
        shoppingCart.applyDiscounts(campaign1, campaign2, campaign3);

        Assert.isNull(shoppingCart.getCartCampaign(), "Minimum quantity control is not valid, campaign shouldn't be applied.");
    }
}
