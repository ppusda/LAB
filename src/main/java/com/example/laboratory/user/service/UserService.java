package com.example.laboratory.user.service;


import com.example.laboratory.global.crypto.ScryptPasswordEncoder;
import com.example.laboratory.global.exception.AlreadyExistsEmailException;
import com.example.laboratory.global.exception.InvalidRequest;
import com.example.laboratory.global.exception.InvalidSigninInformation;
import com.example.laboratory.global.exception.UserNotFoundException;
import com.example.laboratory.token.repository.TokenRepository;
import com.example.laboratory.token.util.TokenRefresher;
import com.example.laboratory.user.editor.UserEditor;
import com.example.laboratory.user.entity.User;
import com.example.laboratory.user.repository.UserRepository;
import com.example.laboratory.user.request.UserForm;
import com.example.laboratory.user.response.KakaoTokenResponse;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TokenRefresher tokenRefresher;
    private final ScryptPasswordEncoder scryptPasswordEncoder;

    public User getUser(Long uid) {
        return userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public Long userLogin(UserForm userForm) {
        User user = userRepository.findByEmail(userForm.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        if(!scryptPasswordEncoder.matches(userForm.getPassword(), user.getPassword())) {
            throw new InvalidSigninInformation();
        }

        if(!user.getTokens().isEmpty()) {
            tokenRefresher.removeRefreshToken(user);
        }

        tokenRefresher.addRefreshToken(user);

        return user.getId();
    }

}

