package com.karhacter.movies_webapp.payloads;

import java.sql.Date;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private long id;
    private String content;
    private Date createdAt;
}
