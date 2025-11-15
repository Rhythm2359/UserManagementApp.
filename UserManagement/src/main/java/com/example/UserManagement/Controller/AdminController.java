package com.example.UserManagementApp.Controller;

import com.example.UserManagement.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.UserManagement.DTO.RegisterRequestDTO;
import com.example.UserManagement.DTO.UserDTO;
import com.example.UserManagement.Service.AuthService;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')") // Restrict access to ADMIN role only
public class AdminController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registeradminuser")
    public ResponseEntity<UserDTO> registerAdminUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        // Call service to register new admin user and return response
        return ResponseEntity.ok(authService.registerAdminUser(registerRequestDTO));
    }
}
