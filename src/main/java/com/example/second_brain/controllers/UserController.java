package com.example.second_brain.controllers;

import com.example.second_brain.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    /**
     * Get current user profile
     * Returns information about the authenticated user
     *
     * URL: GET /api/user/profile
     * Headers: Authorization: Bearer <token>
     * This demonstrates how to get the current authenticated user
     * in any controller method
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile() {
        // Get authenticated user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Build response (don't send password!)
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("username", user.getUsername());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }
    /**
     * Example protected endpoint
     * Only authenticated users can access this
     *
     * URL: GET /api/user/welcome
     * Headers: Authorization: Bearer <token>
     */
    @GetMapping("/welcome")
    public ResponseEntity<?> welcome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome " + user.getUsername() + "!");

        return ResponseEntity.ok(response);
    }
}
