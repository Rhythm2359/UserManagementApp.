package com.example.UserManagement.Controller;

import com.example.UserManagement.DTO.ChangePasswordDTO;
import com.example.UserManagement.DTO.UserDTO;
import com.example.UserManagement.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService ;

    @GetMapping("/getuserbyid/{id}")
    public ResponseEntity<UserDTO> getUserByID(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id)) ;
    }

    @GetMapping("/getuserbyusername/{username}")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserByUsername(username)) ;
    }

    @GetMapping("/getallusers")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers()) ;
    }

    @PutExchange("/changepassword/{id}")
    public ResponseEntity<UserDTO> changePassword(@PathVariable Long id , @RequestBody ChangePasswordDTO changePasswordDTO){
        return ResponseEntity.ok(userService.changePassword(id , changePasswordDTO)) ;
    }

    @PutMapping("/updateuser/{id}")
    public  ResponseEntity<UserDTO> updateUser(@PathVariable Long id , @RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userService.updateUser(id , userDTO)) ;
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id) ;
        return ResponseEntity.ok().build() ;
    }

}
