package com.example.UserManagement.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserDTO {

    private Long id ;

    @Size(min=3 , max=50)
    private String username ;

    @Size(max=70)
    @Email
    private String email ;
}
