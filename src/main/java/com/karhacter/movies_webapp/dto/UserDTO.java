package com.karhacter.movies_webapp.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private long userID;
    private String name;
    private String mobileNumber;
    private String email;
    private String avatar;
    private String password;
    private int balance;
    private LocalDateTime createdAt;
    private Set<Long> roleIds = new HashSet<>();
}