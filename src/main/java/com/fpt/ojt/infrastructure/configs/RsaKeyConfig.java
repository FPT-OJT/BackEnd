package com.fpt.ojt.infrastructure.configs;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RsaKeyConfig {

    @Value("${JWT_PUBLIC_KEY}")
    private String publicKey;

    @Bean
    public RSAPublicKey rsaPublicKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String pem = publicKey;
        log.info("RSA public key: {}", pem);
        String stripped = pem.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(stripped);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        log.info("RSA public key loaded successfully");
        return (RSAPublicKey) kf.generatePublic(spec);
    }
}
