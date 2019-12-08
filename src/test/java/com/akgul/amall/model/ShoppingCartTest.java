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
        Assert.isTrue(discountAmount.equals(BigDecimal.valueOf(12.5)), "50 total price's 25% should be 12.5");
    }
}
