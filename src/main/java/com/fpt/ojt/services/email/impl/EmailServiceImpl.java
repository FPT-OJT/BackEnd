package com.fpt.ojt.services.email.impl;

import com.fpt.ojt.services.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Async("taskExecutor")
    public CompletableFuture<Void> sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Password Reset Request - FPT OJT");

            // Build HTML content from template
            Context context = new Context();
            context.setVariable("otp", otp);
            String htmlContent = templateEngine.process("email/password-reset-otp", context);

            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("OTP email sent successfully to {}", toEmail);
            return CompletableFuture.completedFuture(null);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to {}", toEmail, e);
            return CompletableFuture.failedFuture(e);
        }
    }
}
