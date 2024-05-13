package com.example.laboratory.token.repository;

import com.example.laboratory.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findByUserId(Long userId);

    Optional<Token> deleteByUserId(Long userId);
}
