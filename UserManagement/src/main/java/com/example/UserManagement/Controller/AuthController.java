package com.example.UserManagement.Controller;

import com.example.UserManagement.DTO.LoginRequestDTO;
import com.example.UserManagement.DTO.LoginResponseDTO;
import com.example.UserManagement.DTO.RegisterRequestDTO;
import com.example.UserManagement.DTO.UserDTO;
import com.example.UserManagement.Entity.User;
import com.example.UserManagement.Repository.UserRepository;
import com.example.UserManagement.Service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    // Register new user
    @PostMapping("/registernormaluser")
    public ResponseEntity<UserDTO> registerNormalUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        return ResponseEntity.ok(authService.registerNormalUser(registerRequestDTO));
    }

    // Login and return JWT cookie + user info
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);

        ResponseCookie cookie = ResponseCookie.from("JWT", loginResponseDTO.getJwtToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(60 * 60)
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponseDTO.getUserDTO());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return authService.logout();
    }

    // Get current logged-in user
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");

        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(convertToUserDTO(user));
    }

    // Convert User entity to DTO
    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
