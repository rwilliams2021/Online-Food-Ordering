package com.richard.controller.order;

import com.richard.enums.OrderStatus;
import com.richard.model.Order;
import com.richard.model.User;
import com.richard.service.OrderService;
import com.richard.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/order")
public class AdminOrderController {
    
    private final OrderService orderService;
    private final UserService userService;
    
    public AdminOrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }
    
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<List<Order>> getOrdersByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) OrderStatus orderStatus,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found."));
        List<Order> orders = orderService.getOrdersByRestaurant(restaurantId, orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
    
    @PutMapping("restaurant/{id}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @PathVariable OrderStatus orderStatus,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found."));
        Order order = orderService.updateOrder(id, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
