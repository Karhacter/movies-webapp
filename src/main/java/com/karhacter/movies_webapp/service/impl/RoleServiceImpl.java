package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.Role;
import com.karhacter.movies_webapp.exception.ResourceNotFoundException;
import com.karhacter.movies_webapp.dto.RoleDTO;
import com.karhacter.movies_webapp.repository.RoleRepo;
import com.karhacter.movies_webapp.service.RoleService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepo roleRepo, ModelMapper modelMapper) {
        this.roleRepo = roleRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        role = roleRepo.save(role);
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepo.findAll();
        return roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO updateRole(Long roleId, RoleDTO roleDTO) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));

        role.setRoleName(roleDTO.getRoleName());
        role.setRoleStatus(roleDTO.getRoleStatus());

        Role updatedRole = roleRepo.save(role);
        return modelMapper.map(updatedRole, RoleDTO.class);
    }

    @Override
    public String deleteRole(Long roleId) {
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "roleId", roleId));
        roleRepo.delete(role);
        return "Role with roleId " + roleId + " deleted successfully!";
    }

}
