package com.karhacter.movies_webapp.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.karhacter.movies_webapp.dto.CommentDTO;
import com.karhacter.movies_webapp.entity.Comment;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom mapping to resolve ambiguity for Comment -> CommentDTO userId mapping
        modelMapper.addMappings(new PropertyMap<Comment, CommentDTO>() {
            @Override
            protected void configure() {
                map().setUserId(source.getUser().getUserID());
            }
        });

        // Custom mapping for Episode -> EpisodeDTO movieId mapping
        modelMapper.addMappings(
                new PropertyMap<com.karhacter.movies_webapp.entity.Episode, com.karhacter.movies_webapp.dto.EpisodeDTO>() {
                    @Override
                    protected void configure() {
                        map().setMovieId(source.getMovie().getId());
                    }
                });

        return modelMapper;
    }
}
