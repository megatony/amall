package com.akgul.amall.model;


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
}
