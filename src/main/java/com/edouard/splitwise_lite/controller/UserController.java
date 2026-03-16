package com.edouard.splitwise_lite.controller;

import com.edouard.splitwise_lite.dto.RegisterRequest;
import com.edouard.splitwise_lite.dto.UserResponse;
import com.edouard.splitwise_lite.entity.User;
import com.edouard.splitwise_lite.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
        User user = userService.register(
                request.getEmail(),
                request.getDisplayName(),
                request.getPassword()
        );

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}