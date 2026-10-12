package com.aymen.saas.mappers;

import com.aymen.saas.entities.User;
import com.aymen.saas.requests.UserRequest;
import com.aymen.saas.responses.UserResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {


    public User toEntity(final UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .role(userRequest.getRole())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .build();
    }

    public UserResponse toResponse(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


}
