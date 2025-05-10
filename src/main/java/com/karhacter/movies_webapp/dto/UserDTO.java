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
    private Integer statusDelete;
    private String avatar;
    private String password;
    private int balance;
    private LocalDateTime createdAt;
    private Set<Long> roleIds = new HashSet<>();

    // Added single roleId field to hold single role ID from JSON
    private Long roleId;

    /**
     * Converts the single roleId to the Set of roleIds.
     * If roleId is not null, adds it to roleIds set.
     */
    public void convertRoleIdToRoleIds() {
        if (this.roleId != null) {
            this.roleIds.add(this.roleId);
        }
    }
}
