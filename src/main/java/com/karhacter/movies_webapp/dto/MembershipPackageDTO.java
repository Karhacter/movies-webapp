package com.karhacter.movies_webapp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembershipPackageDTO {
    private Long id;
    private String name;
    private double price;
    private int durationDays;
}
