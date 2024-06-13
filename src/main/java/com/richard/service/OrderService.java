package com.richard.service;

import com.richard.enums.OrderStatus;
import com.richard.model.Order;
import com.richard.model.User;
import com.richard.request.OrderRequest;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderRequest req, User user) throws Exception;
    void deleteOrder(Long orderId) throws Exception;
    Order updateOrder(Long orderId, OrderStatus orderStatus) throws Exception;
    void cancelOrder(Long orderId) throws Exception;
    List<Order> getOrdersByUser(Long userId);
    List<Order> getOrdersByRestaurant(Long restaurantId, OrderStatus orderStatus);
    Order findOrderById(Long orderId) throws Exception;
    void deleteAllOrdersByUser(Long userId);
}
