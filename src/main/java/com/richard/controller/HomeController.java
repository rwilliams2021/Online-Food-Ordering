package com.richard.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class HomeController {
    public ResponseEntity<String> homeController() {
        return ResponseEntity.ok("Welcome to Food Ordering App");
    }
}
