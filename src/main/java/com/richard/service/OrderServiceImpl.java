package com.richard.service;

import com.richard.enums.OrderStatus;
import com.richard.model.*;
import com.richard.request.OrderRequest;
import com.richard.respository.RestaurantRepository;
import com.richard.respository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantService;
    private final CartService cartService;
    
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            UserService userService,
                            AddressRepository addressRepository,
                            UserRepository userRepository,
                            RestaurantRepository restaurantService,
                            CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userService = userService;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.restaurantService = restaurantService;
        this.cartService = cartService;
    }
    
    @Override
    public Order createOrder(OrderRequest req, User user) throws Exception {
        
        Order order = new Order();
        order.setCustomer(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PENDING);
        
        Address address = req.getDeliveryAddress();
        
        Address finalAddress = address;
        Optional<Address> existingAddress = user.getAddresses().stream()
                                                .filter(addr -> addr.equals(finalAddress))
                                                .findFirst();
        
        if (existingAddress.isPresent()) {
            address = addressRepository.findById(existingAddress.get().getId()).orElse(address);
        } else {
            user.getAddresses().add(address);
            userRepository.save(user);
            addressRepository.save(address);
        }
        
        order.setDeliveryAddress(address);
        
        Restaurant restaurant = restaurantService.findById(req.getRestaurantId())
                                                 .orElseThrow(() -> new Exception("Restaurant with id " +
                                                                                  req.getRestaurantId() +
                                                                                  " not found"));
        order.setRestaurant(restaurant);
        
        Cart cart = cartService.findCartByUserId(user.getId());
        
        List<OrderItem> orderItems = new ArrayList<>();
        
        cart.getItems().forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setIngredients(cartItem.getIngredients());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());
            
            OrderItem savedOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(savedOrderItem);
        });
        
        order.setItems(orderItems);
        order.setTotalPrice(cartService.calculateCartTotal(cart));
        
        Order savedOrder = orderRepository.save(order);
        restaurant.getOrders().add(savedOrder);
        
        return order;
    }
    
    @Override
    public void deleteOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found."));
        orderRepository.delete(order);
    }
    
    @Override
    public Order updateOrder(Long orderId, OrderStatus orderStatus) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found."));
        order.setOrderStatus(orderStatus);
        return orderRepository.save(order);
    }
    
    @Override
    public void cancelOrder(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found."));
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
    
    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findAllByCustomerId(userId);
    }
    
    @Override
    public List<Order> getOrdersByRestaurant(Long restaurantId, OrderStatus orderStatus) {
        List<Order> orders = orderRepository.findAllByRestaurantId(restaurantId);
        if (orderStatus != null) {
            orders = orders.stream().filter(order -> order.getOrderStatus().equals(orderStatus)).toList();
        }
        return orders;
    }
    
    @Override
    public Order findOrderById(Long orderId) throws Exception {
        return orderRepository.findById(orderId).orElseThrow(() -> new Exception("Order not found."));
    }
    
    @Transactional
    @Override
    public void deleteAllOrdersByUser(Long userId) {
        orderRepository.deleteAllByCustomerId(userId);
    }
}
