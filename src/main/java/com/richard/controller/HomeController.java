package com.richard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    public ResponseEntity<String> homeController() {
        return ResponseEntity.ok("Welcome to Food Ordering App");
    }
}
