package com.email_sender.service;

import jakarta.mail.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class EmailSchedulerService {

    private final EmailService emailService;

    public EmailSchedulerService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendNow(String subject, String from, String content, List<String> recipients, boolean isHtml) {
        try {
            emailService.sendEmail(subject, from, content, recipients, isHtml);
        } catch (MessagingException e) {
            e.printStackTrace();

        }
    }

    public void scheduleEmail(String subject, String from, String content, List<String> recipients, boolean isHtml, LocalDateTime scheduleTime) {
        long delay = java.time.Duration.between(LocalDateTime.now(), scheduleTime).toSeconds();

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            try {
                emailService.sendEmail(subject, from, content, recipients, isHtml);
            } catch (MessagingException e) {
                e.printStackTrace();

            }
        }, delay, TimeUnit.SECONDS);
    }
}

