package com.example.BlogProject.services;

import com.example.BlogProject.entities.RefreshToken;
import com.example.BlogProject.exceptions.SpringBlogException;
import com.example.BlogProject.repositories.RefreshTokenRespository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRespository refreshTokenRespository;

    RefreshToken generateRefreshToken(){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setCreatedDate(Instant.now());

        return refreshTokenRespository.save(refreshToken);
    }

    public void validateRefreshToken(String token) throws SpringBlogException {
        refreshTokenRespository.findByToken(token)
                //change this exception to a blog post exception?
                .orElseThrow(() -> new SpringBlogException("Invalid Refresh Token"));

    }

    public void deleteRefreshToken(String token){
        refreshTokenRespository.deleteByToken(token);
    }

}
