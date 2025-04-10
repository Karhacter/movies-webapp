package com.karhacter.movies_webapp.dto;

import java.util.Date;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistDTO {
    private Long id;
    private Date created_at = new Date();
    private Long userId;
    private Long movieId;
}
