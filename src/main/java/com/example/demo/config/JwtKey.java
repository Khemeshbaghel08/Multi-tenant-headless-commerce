package com.example.demo.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.*;

@Configuration
public class JwtKey {

    @Bean
    public KeyPair keyPair () {
        return Keys.keyPairFor(SignatureAlgorithm.RS256);
    }

    @Bean
    public PrivateKey privateKey (KeyPair keyPair) {
        return keyPair.getPrivate();
    }

    @Bean
    public PublicKey publicKey (KeyPair keyPair) {
        return keyPair.getPublic();
    }
}
