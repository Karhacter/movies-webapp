package com.karhacter.movies_webapp.payloads;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private long menuId;
    private String name;
    private String link;
    private Integer sortOrder;
    private String position;
    private String menuType;
    private String parentId;
}
