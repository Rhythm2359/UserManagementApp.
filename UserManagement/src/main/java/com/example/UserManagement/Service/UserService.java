package com.example.UserManagement.Service;

import com.example.UserManagement.DTO.ChangePasswordDTO;
import com.example.UserManagement.DTO.UserDTO;
import com.example.UserManagement.Entity.User;
import com.example.UserManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository ;

    @Autowired
    private PasswordEncoder passwordEncoder ;

    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("user not found")) ;

        UserDTO userDTO = convertUserToDTO(user) ;
        return userDTO ;
    }

    public UserDTO getUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("user not found"));
        return convertUserToDTO(user);
    }

    public List<UserDTO> getAllUsers(){
        List<User> list = userRepository.findAll() ;

        return list.stream().map(this::convertUserToDTO).collect(Collectors.toList());
    }

    public UserDTO changePassword(Long id , ChangePasswordDTO changePasswordDTO){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("user not found"));

        if(!passwordEncoder.matches(user.getPassword() , changePasswordDTO.getCurrentPassword())){
            throw new RuntimeException("current password not matched") ;
        }

        if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
            throw new RuntimeException("new password doesnt match confirm password");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        User saved = userRepository.save(user);
        return convertUserToDTO(saved) ;
    }

    public UserDTO updateUser(Long id , UserDTO userDTO){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("user not found")) ;

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        User saved = userRepository.save(user);
        return convertUserToDTO(saved) ;
    }

    public Void deleteUser(Long id){
        userRepository.deleteById(id);
        return null ;
    }

    public UserDTO convertUserToDTO(User user){
        UserDTO userDTO = new UserDTO() ;
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO ;
    }
}
