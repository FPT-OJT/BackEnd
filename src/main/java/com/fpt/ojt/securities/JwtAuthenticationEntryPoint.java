package com.fpt.ojt.securities;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final String errorMessage;
        final String errorCode;

        if (request.getAttribute("expired") != null) {
            errorCode = "TOKEN_EXPIRED";
            errorMessage = "Access token has expired. Please refresh your token.";
        } else if (request.getAttribute("malformed") != null) {
            errorCode = "TOKEN_MALFORMED";
            errorMessage = "Invalid JWT token format.";
        } else if (request.getAttribute("signature") != null) {
            errorCode = "TOKEN_SIGNATURE_INVALID";
            errorMessage = "JWT signature does not match locally computed signature.";
        } else if (request.getAttribute("unsupported") != null) {
            errorCode = "TOKEN_UNSUPPORTED";
            errorMessage = "JWT token is not supported.";
        } else if (request.getAttribute("empty") != null) {
            errorCode = "TOKEN_EMPTY";
            errorMessage = "JWT claims string is empty.";
        } else {
            errorCode = "UNAUTHORIZED";
            errorMessage = "Full authentication is required to access this resource.";
        }

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", errorCode);
        body.put("message", errorMessage);
        body.put("path", request.getServletPath());

        objectMapper.writeValue(response.getOutputStream(), body);
    }
}