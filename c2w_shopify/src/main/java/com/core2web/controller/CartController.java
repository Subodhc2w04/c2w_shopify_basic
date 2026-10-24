package com.core2web.controller;

import java.util.List;

import com.core2web.dao.CartDao;
import com.core2web.dao.ProductDao;
import com.core2web.model.CartItem;
import com.core2web.model.Product;

public class CartController {

    CartDao cartDao = new CartDao();
    ProductDao productDao = new ProductDao();

    // Returns null on success, otherwise an error message to show on the screen
    public String addToCart(String email, Product product) {
        if (product.getStock() <= 0) {
            return "\"" + product.getName() + "\" is out of stock.";
        }
        CartItem existing = cartDao.getCartItem(email, product.getId());
        int alreadyInCart = existing == null ? 0 : existing.getQuantity();
        if (alreadyInCart + 1 > product.getStock()) {
            return "Only " + product.getStock() + " of \"" + product.getName() + "\" available.";
        }

        CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl(), 1);
        return cartDao.addToCart(email, item) ? null : "Could not add to cart. Please try again.";
    }

    // Returns null on success, otherwise an error message to show on the screen
    public String changeQuantity(String email, CartItem item, int newQuantity) {
        if (newQuantity <= 0) {
            return cartDao.removeFromCart(email, item.getProductId()) ? null : "Could not update the cart.";
        }
        Product product = productDao.getProductById(item.getProductId());
        if (product == null) {
            return "This product is no longer available.";
        }
        if (newQuantity > product.getStock()) {
            return "Only " + product.getStock() + " of \"" + product.getName() + "\" available.";
        }
        return cartDao.updateQuantity(email, item.getProductId(), newQuantity) ? null : "Could not update the cart.";
    }

    public boolean removeFromCart(String email, String productId) {
        return cartDao.removeFromCart(email, productId);
    }

    public List<CartItem> getCartItems(String email) {
        return cartDao.getCartItems(email);
    }

    public int getCartCount(String email) {
        int count = 0;
        for (CartItem item : cartDao.getCartItems(email)) {
            count += item.getQuantity();
        }
        return count;
    }

    public boolean clearCart(String email) {
        return cartDao.clearCart(email);
    }
}
