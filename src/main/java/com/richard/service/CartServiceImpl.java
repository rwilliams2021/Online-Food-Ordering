package com.richard.service;

import com.richard.model.Cart;
import com.richard.model.CartItem;
import com.richard.model.Food;
import com.richard.model.User;
import com.richard.request.AddCartItemRequest;
import com.richard.respository.CartItemRepository;
import com.richard.respository.CartRepository;
import com.richard.respository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private FoodService foodService;
    
    @Autowired
    private UserService userService;
    
    @Override
    public CartItem addItemToCart(AddCartItemRequest req, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found."));
        Cart cart = cartRepository.findByCustomerId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setCustomer(user);
        }
        Food food = foodService.findFoodById(req.getFoodId());
        
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getFood().equals(food)) {
                int newQuantity = cartItem.getQuantity() + req.getQuantity();
                return updateCartItemQuantity(cartItem.getId(), newQuantity);
            }
        }
        
        CartItem newCartItem = new CartItem();
        newCartItem.setCart(cart);
        newCartItem.setFood(food);
        newCartItem.setQuantity(req.getQuantity());
        newCartItem.setIngredients(req.getIngredients());
        newCartItem.setTotalPrice(req.getQuantity() * food.getPrice());
        cart.getItems().add(newCartItem);
        
        return cartItemRepository.save(newCartItem);
    }
    
    @Override
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) throws Exception {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new Exception("Cart item not found."));
        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * cartItem.getFood().getPrice());
        return cartItemRepository.save(cartItem);
    }
    
    @Override
    public Cart removeItemFromCart(Long cartItemId, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found."));
        Cart cart = cartRepository.findByCustomerId(user.getId());
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() -> new Exception("Cart item not found."));
        cart.getItems().remove(cartItem);
        return cartRepository.save(cart);
    }
    
    @Override
    public Long calculateCartTotal(Cart cart) {
        return cart.getItems().stream()
                   .mapToLong(CartItem::getTotalPrice)
                   .sum();
    }
    
    @Override
    public Cart findCartById(Long cartId) throws Exception {
        return cartRepository.findById(cartId).orElseThrow(() -> new Exception("Cart not found with id " + cartId));
    }
    
    @Override
    public Cart findCartByUserId(Long userId) {
        return cartRepository.findByCustomerId(userId);
    }
    
    @Override
    public Cart clearCart(Long userId) {
        Cart cart = findCartByUserId(userId);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }
}
