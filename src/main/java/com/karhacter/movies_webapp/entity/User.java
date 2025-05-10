package com.karhacter.movies_webapp.entity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.micrometer.common.lang.Nullable;
import lombok.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    @Column(name = "status_delete")
    private Integer statusDelete = 1;

    private boolean isPremium = false;

    private java.time.LocalDateTime membershipExpiration;

    @Size(min = 2, max = 50, message = "Name must be between 2 and 20 characters")
    private String name;

    @Size(min = 10, max = 10, message = "Moblie Number must be exactly 10 digits long")
    @Pattern(regexp = "^\\d{10}$", message = "Moblie Number must contain only numbers")
    private String mobileNumber;

    @Nullable
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Nullable
    private String avatar;

    @Size(min = 5)
    @Column(nullable = true)
    private String password;

    private int balance;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Watchlist> watchlist;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<History> history;

    @Nullable
    private String provider;

    @Nullable
    private String googleId;

    @Nullable
    private String facebookId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getUsername() {
        return email;
    }
}
