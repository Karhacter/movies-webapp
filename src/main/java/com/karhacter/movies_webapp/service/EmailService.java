package com.karhacter.movies_webapp.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
