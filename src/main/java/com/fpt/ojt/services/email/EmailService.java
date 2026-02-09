package com.fpt.ojt.services.email;

import java.util.concurrent.CompletableFuture;

public interface EmailService {
    CompletableFuture<Void> sendOtpEmail(String toEmail, String otp);
}
