package com.karhacter.movies_webapp.payloads;

import java.util.List;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieResponse {
    private List<MovieDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
}
