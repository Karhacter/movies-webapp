package com.karhacter.movies_webapp.entity;

import java.util.List;

import javax.management.relation.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column(name = "userID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;
    
    @Size(min = 3, max = 50, message = "Username must be between 3 and 20 characters")
    @Pattern(regexp = "[a-zA-Z]*$", message = "First Name must not contain numbers or special characters")
    @Column(name = "name")
    private String name;

    @Size(min = 10, max = 10, message = "Moblie Number must be exactly 10 digits long")
    @Pattern(regexp = "^\\d{10}$", message = "Moblie Number must contain only numbers")
    private String moblieNumber;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false, name = "email")
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Watchlist> watchlist;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<History> history;
}
