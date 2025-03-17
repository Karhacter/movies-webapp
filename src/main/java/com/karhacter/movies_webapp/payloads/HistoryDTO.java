package com.karhacter.movies_webapp.payloads;

import java.util.Date;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDTO {
    private Long id;
    private int watchProgress;
    private Date lastWatched = new Date();
    private Long userId;
    private Long movieId;
}
