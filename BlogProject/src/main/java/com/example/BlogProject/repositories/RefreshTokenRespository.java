package com.example.BlogProject.repositories;

import com.example.BlogProject.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRespository extends JpaRepository<RefreshToken, Long> {
    void deleteByToken(String token);

    Optional<RefreshToken> findByToken(String token);

    RefreshToken save(RefreshToken refreshToken);

}
