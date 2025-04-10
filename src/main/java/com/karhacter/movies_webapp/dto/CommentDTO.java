package com.karhacter.movies_webapp.dto;

import java.util.Date;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long id;
    private String content;
    private Date createdAt;
    private Long userId;
    private Long movieId;
    private String userName;
    private String userAvatar;
}
