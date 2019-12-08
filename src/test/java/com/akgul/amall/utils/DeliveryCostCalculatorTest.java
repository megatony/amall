package com.akgul.amall.utils;

import com.akgul.amall.model.Category;
import com.akgul.amall.model.Product;
import com.akgul.amall.model.ShoppingCart;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;

public class DeliveryCostCalculatorTest {

    @Test
    public void shouldCalculateDeliveryCostForCart() {
        Category category = new Category("TestCategory");
        Product product = new Product("TestProduct", BigDecimal.ONE, category);
        product.setQuantity(10);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.addItem(product, 1);

        BigDecimal costPerDelivery = BigDecimal.valueOf(2.0);
        BigDecimal costPerProduct = BigDecimal.valueOf(3.0);
        BigDecimal fixedCost = BigDecimal.valueOf(2.99);
        double expectedDeliveryCost = costPerDelivery.add(costPerProduct).add(fixedCost).doubleValue();
        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(costPerDelivery, costPerProduct, fixedCost);

        double deliveryCost = deliveryCostCalculator.calculateFor(shoppingCart);
        Assert.isTrue(deliveryCost == expectedDeliveryCost, "One category, one product with fixed cost delivery should be " + expectedDeliveryCost );
    }

    @Test
    public void shouldTwoCostPerDeliveryWhenTwoDistinctCategoryProductIsOnCart() {
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

        DeliveryCostCalculator deliveryCostCalculator = new DeliveryCostCalculator(costPerDelivery, costPerProduct, fixedCost);
        double deliveryCost = deliveryCostCalculator.calculateFor(shoppingCart);

        Assert.isTrue(deliveryCost == expectedDeliveryCost, "Two category, two product with fixed cost delivery should be " + expectedDeliveryCost );

    }
}
