package com.aymen.saas.services.impl;

import com.aymen.saas.common.PageResponse;
import com.aymen.saas.config.TenantContext;
import com.aymen.saas.entities.Tenant;
import com.aymen.saas.entities.User;
import com.aymen.saas.entities.UserRole;
import com.aymen.saas.exceptions.DuplicateRessourceException;
import com.aymen.saas.exceptions.InvalidRequestException;
import com.aymen.saas.mappers.UserMapper;
import com.aymen.saas.repositories.TenantRepository;
import com.aymen.saas.repositories.UserRepository;
import com.aymen.saas.requests.UserRequest;
import com.aymen.saas.responses.UserResponse;
import com.aymen.saas.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final  TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;




    @Override
    public void createUser(UserRequest request) {

         final String tenantId = TenantContext.getCurrentTenant();
         // check if username exists
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateRessourceException("Username is already in use");
        }
        // check if email exists
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateRessourceException("Email is already in use");
        }
        // validate Role (cannot be a platformAdmin)
        if( request.getRole() == UserRole.ROLE_PLATFORM_ADMIN){
            throw new InvalidRequestException("Role is not allowed to use platform admin");
        }
        final User user = this.userMapper.toEntity(request);
        user.setTenant(Tenant.builder().id(tenantId).build());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        log.info("User created successfully");
    }

    @Override
    public void updateUser(String userId, UserRequest request) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Updating User for tenant : {}", tenantId);

        final User user =
                this.userRepository.findByIdAndNotDeleted(userId)
                        .orElseThrow(()-> new DuplicateRessourceException("User not found"));
        // check if user belong the right tenant
        if(!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to this tenant");
        }
        // check username is beign changed and if it is already taken
        if(!user.getUsername().equals(request.getUsername()) && this.userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateRessourceException("Username is already in use");
        }
        // check if email is being changed and if it is already taken
        if (!user.getEmail().equals(request.getEmail()) && this.userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateRessourceException("Email already exists");
        }

        // validate role (cannot be PLATFORM_ADMIN)
        if (request.getRole() == UserRole.ROLE_PLATFORM_ADMIN) {
            throw new InvalidRequestException("Role is required");
        }

        // update user details
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        this.userRepository.save(user);
        log.info("User updated successfully");

    }

    @Override
    public void deleteUser(final String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        log.info("Deleting user for tenant: {}", tenantId);

        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        // soft delete user
        user.setDeleted(true);
        this.userRepository.save(user);
        log.info("User deleted successfully");
    }

    @Override
    public UserResponse getUserById(final String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }
        return this.userMapper.toResponse(user);
    }

    @Override
    public PageResponse<UserResponse> getAllUsers(final int page, final int size) {
        final String tenantId = TenantContext.getCurrentTenant();
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Page<User> userPage = this.userRepository.findAllByTenantId(tenantId, pageRequest);
        final Page<UserResponse> userResponses = userPage.map(this.userMapper::toResponse);
        return PageResponse.of(userResponses);
    }

    @Override
    public void enableUser(final String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(true);
        this.userRepository.save(user);
        log.info("User enabled successfully");
    }

    @Override
    public void disableUser(final String userId) {
        final String tenantId = TenantContext.getCurrentTenant();
        final User user = this.userRepository.findByIdAndNotDeleted(userId)
                .orElseThrow(() -> new EntityNotFoundException("User does not exist"));

        // check if user belongs to the tenant
        if (!user.getTenant().getId().equals(tenantId)) {
            throw new InvalidRequestException("User does not belong to the tenant");
        }

        user.setEnabled(false);
        this.userRepository.save(user);
        log.info("User disabled successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
