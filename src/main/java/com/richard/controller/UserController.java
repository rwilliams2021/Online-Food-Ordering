package com.richard.controller;

import com.richard.model.User;
import com.richard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    
    private final UserService userService;
    
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @Operation(
            summary = "Find User by JWT Token",
            description = "Fetches the user profile associated with the provided JWT token.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                         content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access, invalid JWT token",
                         content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content)
    })
    @GetMapping("/profile")
    public ResponseEntity<User> findUserByJwtToken(
            @Parameter(description = "JWT token used to authenticate the user", required = true)
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt).orElseThrow(() -> new Exception("User not found."));
        return ResponseEntity.ok(user);
    }
    
    @Operation(
            summary = "Get All Users",
            description = "Retrieves a list of all users."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                         content = @Content(mediaType = "application/json",
                                            schema = @Schema(implementation = User.class)))
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    
    @Operation(
            summary = "Delete User by ID",
            description = "Deletes a user identified by the provided ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                         content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                         content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @Parameter(description = "ID of the user to be deleted", required = true)
            @PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
