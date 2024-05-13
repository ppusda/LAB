package com.example.laboratory.token.util;

import com.example.laboratory.global.provider.JwtTokenProvider;
import com.example.laboratory.token.repository.TokenRepository;
import com.example.laboratory.user.entity.User;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TokenRefresher {

    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void removeRefreshToken(User user) {
        tokenRepository.deleteByUserId(user.getId());
    }

    @Transactional
    public void addRefreshToken(User user) {
        user.addToken(jwtTokenProvider.createRefreshToken(user.getId()));
    }

}
