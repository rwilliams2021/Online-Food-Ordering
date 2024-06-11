package com.richard.service;

import com.richard.model.Cart;
import com.richard.model.CartItem;
import com.richard.request.AddCartItemRequest;

public interface CartService {
    CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception;
    CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception;
    Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception;
    Double calculateCartTotal(Cart cart);
    Cart findCartById(Long cartId) throws Exception;
    Cart findCartByUserId(Long userId);
    Cart clearCart(Long userId);
}
