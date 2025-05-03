package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.dto.RoleDTO;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO getRoleById(Long roleId);

    List<RoleDTO> getAllRoles();

    RoleDTO updateRole(Long roleId, RoleDTO roleDTO);

    String deleteRole(Long roleId);
}
