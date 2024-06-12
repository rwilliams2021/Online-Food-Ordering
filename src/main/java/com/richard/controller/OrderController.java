package com.richard.controller;

import com.richard.model.Order;
import com.richard.model.User;
import com.richard.request.OrderRequest;
import com.richard.response.PaymentResponse;
import com.richard.service.OrderService;
import com.richard.service.PaymentService;
import com.richard.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    private final OrderService orderService;
    private final UserService userService;
    private final PaymentService paymentService;
    
    public OrderController(OrderService orderService, UserService userService, PaymentService paymentService) {
        this.orderService = orderService;
        this.userService = userService;
        this.paymentService = paymentService;
    }
    
    @PostMapping
    public ResponseEntity<PaymentResponse> createOrder(
            @RequestBody OrderRequest req,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found."));
        Order order = orderService.createOrder(req, user);
        PaymentResponse paymentResponse = paymentService.createPaymentLink(order);
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getOrderHistory(
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found."));
        List<Order> orders = orderService.getOrdersByUser(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
