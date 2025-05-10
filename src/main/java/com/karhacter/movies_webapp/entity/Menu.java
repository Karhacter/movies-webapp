package com.karhacter.movies_webapp.entity;

import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "menus")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer menuId;

    @NotBlank
    @Size(min = 3, message = "Menu name must contain at least 3 characters")
    private String menuName;

    @NotBlank
    private String link;

    @Nullable
    @Column(name = "sort_order")
    private Integer sortOrder;

    @Nullable
    private String position;

    @Nullable
    @Column(name = "menu_type")
    private String menuType;

    @NotBlank
    @Column(name = "parent_id")
    private String parentId;
}
