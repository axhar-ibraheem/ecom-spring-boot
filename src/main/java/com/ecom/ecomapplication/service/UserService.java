package com.ecom.ecomapplication.service;

import com.ecom.ecomapplication.dto.AddressDto;
import com.ecom.ecomapplication.dto.UserRequest;
import com.ecom.ecomapplication.dto.UserResponse;
import com.ecom.ecomapplication.model.Address;
import com.ecom.ecomapplication.model.User;
import com.ecom.ecomapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> fetchAllUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(this::mapToUserResponse)
                             .collect(Collectors.toList());
    }

    public Optional<UserResponse> fetchUser(Long userId) {
        return userRepository.findById(userId)
                             .map(this::mapToUserResponse);
    }

    @Transactional
    public boolean updateUser(Long id, UserRequest updatedUser) {
        return userRepository.findById(id)
                             .map(existingUser -> {
                                 existingUser.setFirstName(updatedUser.getFirstName());
                                 existingUser.setLastName(updatedUser.getLastName());
                                 existingUser.setPhone(updatedUser.getPhone());
                                 existingUser.setEmail(updatedUser.getEmail());
                                 updateAddress(existingUser, updatedUser);
                                 return true;
                             })
                             .orElse(false);
    }

    public void addUser(UserRequest userRequest) {
        User user = mapToUser(userRequest);
        userRepository.save(user);
    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDto addressDto = new AddressDto();
            addressDto.setStreet(user.getAddress()
                                     .getStreet());
            addressDto.setCity(user.getAddress()
                                   .getCity());
            addressDto.setState(user.getAddress()
                                    .getState());
            addressDto.setCountry(user.getAddress()
                                      .getCountry());
            addressDto.setZipCode(user.getAddress()
                                      .getZipcode());
            response.setAddress(addressDto);
        }
        return response;
    }

    private User mapToUser(UserRequest userRequest) {
        User user = new User();

        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhone(userRequest.getPhone());
        user.setEmail(userRequest.getEmail());

        if (userRequest.getAddress() != null) {
            Address address = new Address();

            address.setStreet(userRequest.getAddress()
                                         .getStreet());
            address.setCity(userRequest.getAddress()
                                       .getCity());
            address.setState(userRequest.getAddress()
                                        .getState());
            address.setCountry(userRequest.getAddress()
                                          .getCountry());
            address.setZipcode(userRequest.getAddress()
                                          .getZipCode());

            user.setAddress(address);
        }

        return user;
    }

    private void updateAddress(User existingUser, UserRequest updatedUser) {
        if (updatedUser.getAddress() == null) {
            return;
        }
        if (existingUser.getAddress() == null) {
            existingUser.setAddress(new Address());
        }
        existingUser.getAddress()
                    .setStreet(updatedUser.getAddress()
                                          .getStreet());
        existingUser.getAddress()
                    .setCity(updatedUser.getAddress()
                                        .getCity());
        existingUser.getAddress()
                    .setState(updatedUser.getAddress()
                                         .getState());
        existingUser.getAddress()
                    .setCountry(updatedUser.getAddress()
                                           .getCountry());
        existingUser.getAddress()
                    .setZipcode(updatedUser.getAddress()
                                           .getZipCode());
    }
}
