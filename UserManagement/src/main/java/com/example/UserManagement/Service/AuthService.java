package com.example.UserManagement.Service;

import com.example.UserManagement.DTO.LoginRequestDTO;
import com.example.UserManagement.DTO.LoginResponseDTO;
import com.example.UserManagement.DTO.RegisterRequestDTO;
import com.example.UserManagement.DTO.UserDTO;
import com.example.UserManagement.Entity.User;
import com.example.UserManagement.Jwt.JwtService;
import com.example.UserManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    @Autowired
    private AuthenticationManager authenticationManager ;

    @Autowired
    private JwtService jwtService ;


    public UserDTO registerNormalUser(RegisterRequestDTO registerRequestDTO){
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("User is already Registered");
        }

        Set<String> set = new HashSet<>();
        set.add("ROLE_USER") ;

        User user = new User() ;
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(set);

        User savedUser = userRepository.save(user);

        return convertUserToDTO(user) ;
    }

    public UserDTO registerAdminUser(RegisterRequestDTO registerRequestDTO){
        if(userRepository.findByUsername(registerRequestDTO.getUsername()).isPresent()){
            throw new RuntimeException("admin already exist") ;
        }

        Set<String> set = new HashSet<>() ;
        set.add("ROLE_ADMIN") ;
        set.add("ROLE_USER") ;

        User user = new User() ;
        user.setUsername(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(set);

        User savedUser = userRepository.save(user);
        return convertUserToDTO(savedUser);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        User user = userRepository.findByUsername(loginRequestDTO.getUsername())
                .orElseThrow(()-> new RuntimeException("user not found")) ;

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequestDTO.getUsername(), loginRequestDTO.getPassword()
        )) ;

        String jwtToken = jwtService.generateToken(user) ;

        return LoginResponseDTO.builder()
                .jwtToken(jwtToken)
                .userDTO(convertUserToDTO(user))
                .build() ;
    }

    public ResponseEntity<String> logout() {
        // Create an expired JWT cookie to remove token from client side
        ResponseCookie cookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        // Return response with expired cookie
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("LOGGED OUT SUCCESSFULLY");
    }


    public UserDTO convertUserToDTO(User user){
        UserDTO userDTO = new UserDTO() ;
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO ;
    }
}
