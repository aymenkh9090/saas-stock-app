package com.aymen.saas.requests;

import com.aymen.saas.entities.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {


    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private UserRole role;



}
