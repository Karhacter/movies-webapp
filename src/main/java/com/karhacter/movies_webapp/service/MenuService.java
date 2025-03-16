package com.karhacter.movies_webapp.service;

import java.util.List;

import com.karhacter.movies_webapp.entity.Menu;
import com.karhacter.movies_webapp.payloads.MenuDTO;

public interface MenuService {
    MenuDTO createMenu(Menu menu);

    List<MenuDTO> getAllMenu();

    MenuDTO getMenuById(Long menuId);

    MenuDTO updateMenu(Long menuId, MenuDTO menuDTO);

    String deleteMenu(Long menuId);
}
