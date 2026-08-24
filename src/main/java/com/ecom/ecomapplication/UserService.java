package com.ecom.ecomapplication;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private List<User> userList = new ArrayList<>();
    private Long nextId = 1L;

    public List<User> fetchAllUsers() {
        return userList;
    }

    public Optional<User> fetchUser(Long userId) {
        return userList.stream()
                       .filter(user -> user.getId()
                                           .equals(userId))
                       .findFirst();
    }

    public boolean updateUser(Long id, User updatedUser) {
        return userList.stream()
                       .filter(user -> user.getId()
                                           .equals(id))
                       .findFirst()
                       .map(existingUser -> {
                           existingUser.setFirstName(updatedUser.getFirstName());
                           existingUser.setLastName(updatedUser.getLastName());
                           return true;
                       })
                       .orElse(false);
    }

    public void addUser(User user) {
        user.setId(nextId++);
        userList.add(user);
    }
}
