package com.karhacter.movies_webapp.service.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.karhacter.movies_webapp.entity.Menu;
import com.karhacter.movies_webapp.exception.APIException;
import com.karhacter.movies_webapp.dto.MenuDTO;
import com.karhacter.movies_webapp.repository.MenuRepo;
import com.karhacter.movies_webapp.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepo menuRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public MenuDTO createMenu(Menu menu) {
        Optional<Menu> existingCategory = Optional
                .ofNullable(menuRepo.findByMenuName(menu.getMenuName()));

        if (existingCategory.isPresent()) {
            throw new APIException("Category with the name '" +
                    menu.getMenuName() + "' already exists !!!");
        }

        // Lưu danh mục mới
        Menu savedMenu = menuRepo.save(menu);

        return modelMapper.map(savedMenu, MenuDTO.class);
    }

    @Override
    public List<MenuDTO> getAllMenu() {
        return menuRepo.findAll().stream()
                .map(menu -> modelMapper.map(menu, MenuDTO.class))
                .toList();
    }

    @Override
    public MenuDTO getMenuById(Long menuId) {

        throw new UnsupportedOperationException("Unimplemented method 'getMenuById'");
    }

    @Override
    public MenuDTO updateMenu(Long menuId, MenuDTO menuDTO) {

        throw new UnsupportedOperationException("Unimplemented method 'updateMenu'");
    }

    @Override
    public String deleteMenu(Long menuId) {

        throw new UnsupportedOperationException("Unimplemented method 'deleteMenu'");
    }

}
