package com.ecom.ecomapplication.controller;

import com.ecom.ecomapplication.dto.UserRequest;
import com.ecom.ecomapplication.dto.UserResponse;
import com.ecom.ecomapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.fetchAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return userService.fetchUser(id)
                          .map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound()
                                                         .build());
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        userService.addUser(userRequest);
        return ResponseEntity.ok("User created Successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserRequest updatedUser) {
        return userService.updateUser(id, updatedUser)
               ? ResponseEntity.ok("User updated successfully!")
               : ResponseEntity.notFound()
                               .build();
    }

}
