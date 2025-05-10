package com.karhacter.movies_webapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, unique = true)
    private String roleName; // Ví dụ: "ADMIN", "USER"

    @Column(nullable = false)
    private Integer roleStatus; // 0 = Super Admin, 1 = Admin, 2 = User (càng nhỏ quyền càng cao)
}