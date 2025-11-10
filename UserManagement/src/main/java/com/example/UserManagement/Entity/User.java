package com.example.UserManagement.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name="users")
@Data

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String username ;
    private  String email ;
    private String password ;
    private boolean isActive = true ;
    private LocalDateTime createdAt ;
    private LocalDateTime updatedAt ;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles ;

    @PrePersist
    public void onCreate(){
        this.createdAt = LocalDateTime.now() ;
        this.updatedAt = LocalDateTime.now() ;
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now() ;
    }
}
