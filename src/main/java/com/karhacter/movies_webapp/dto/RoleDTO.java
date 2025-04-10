package com.karhacter.movies_webapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private Long roleId;

    @JsonProperty("roleName")
    private String roleName;

    @JsonProperty("roleStatus")
    private Integer roleStatus;
}