package com.example.BlogProject.security;


import com.example.BlogProject.exceptions.SpringBlogException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

import static io.jsonwebtoken.Jwts.parser;
import static java.util.Date.from;

@Service
public class JwtProvider {
    private KeyStore keyStore;
    @Value("${jwt.expiration.time}")
    private Long jwtExpirationInMillis;

    @PostConstruct
    public void init() throws SpringBlogException {
        try{
            keyStore = keyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/springreddit.jks");
            keyStore.load(resourceAsStream, "secret".toCharArray());
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e){
            throw new SpringBlogException("Exception occurred while loading keystore", e);
        }
    }

    public String generateToken(Authentication authentication) throws SpringBlogException {
        User principal = (User) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    public String generateTokenWithUserName(String username) throws SpringBlogException {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(from(Instant.now()))
                .signWith(getPrivateKey())
                .setExpiration(from(Instant.now().plusMillis(jwtExpirationInMillis)))
                .compact();
    }

    private PrivateKey getPrivateKey() throws SpringBlogException {
        try{
            return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
            throw new SpringBlogException("Exception occurred while retrieving public key from keystore", e);
        }
    }

    public boolean validateToken(String jwt) throws SpringBlogException {
        parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
        return true;
    }

    private PublicKey getPublicKey() throws SpringBlogException {
        try{
            return keyStore.getCertificate("springblog").getPublicKey();
        } catch (KeyStoreException e){
            throw new SpringBlogException("Exception occurred while " +
                    "retrieving public key from keystore", e);
        }
    }

    public String getUsernameFromJwt(String token) throws SpringBlogException {
        Claims claims = parser()
                .setSigningKey(getPublicKey())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Long getJwtExpirationInMillis(){
        return jwtExpirationInMillis;
    }
}
