package com.example.UserManagement.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String username ;

    @Email
    @Size(min=3 , max=50)
    private String email ;

    @Size(min=3 , max=70)
    private String password ;
}
