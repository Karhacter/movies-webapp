package com.karhacter.movies_webapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.karhacter.movies_webapp.entity.Menu;
import com.karhacter.movies_webapp.payloads.MenuDTO;
import com.karhacter.movies_webapp.service.MenuService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/menus")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @PostMapping("/create")
    public ResponseEntity<MenuDTO> createMenu(@RequestBody Menu menu) {
        MenuDTO createdMenu = menuService.createMenu(menu);
        return ResponseEntity.ok(createdMenu);
    }

    @GetMapping("/detail/{MenuId}")
    public ResponseEntity<MenuDTO> getMenuById(@PathVariable Long MenuId) {
        return new ResponseEntity<>(menuService.getMenuById(MenuId), HttpStatus.OK);
    }

    @GetMapping("/index")
    public ResponseEntity<List<MenuDTO>> getAllMenus() {
        return new ResponseEntity<>(menuService.getAllMenu(), HttpStatus.OK);
    }

    
}
