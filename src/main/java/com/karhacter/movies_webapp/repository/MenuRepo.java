package com.karhacter.movies_webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karhacter.movies_webapp.entity.Menu;

@Repository
public interface MenuRepo extends JpaRepository<Menu, Integer> {
    Menu findByMenuName(String name);
}
