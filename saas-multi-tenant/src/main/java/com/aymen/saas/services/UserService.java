package com.aymen.saas.services;

import com.aymen.saas.common.PageResponse;
import com.aymen.saas.requests.UserRequest;
import com.aymen.saas.responses.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    void createUser(final UserRequest request);
    void updateUser(final String id , final UserRequest request);
    void deleteUser(final String id);
    UserResponse getUserById(final String id);
    PageResponse<UserResponse> getAllUsers(final int page, final int size);
    void enableUser(final String userId);
    void disableUser(final String userId);



}
