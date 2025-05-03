package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.dto.RoleDTO;
import com.karhacter.movies_webapp.service.RoleService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleDTO> createRole(@RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return ResponseEntity.ok(createdRole);
    }

    @GetMapping("/detail/{roleId}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long roleId) {
        return new ResponseEntity<>(roleService.getRoleById(roleId), HttpStatus.OK);
    }

    @GetMapping("/index")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return new ResponseEntity<>(roleService.getAllRoles(), HttpStatus.OK);
    }

    @PutMapping("/update/{roleId}")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long roleId, @RequestBody RoleDTO roleDTO) {
        return new ResponseEntity<>(roleService.updateRole(roleId, roleDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{roleId}")
    public ResponseEntity<String> deleteRole(@PathVariable Long roleId) {
        return new ResponseEntity<>(roleService.deleteRole(roleId), HttpStatus.OK);
    }
}