package com.karhacter.movies_webapp.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "membership_packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double price;

    private int durationDays; // Duration of membership in days
}
