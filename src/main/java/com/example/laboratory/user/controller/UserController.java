package com.example.laboratory.user.controller;

import com.example.laboratory.global.provider.CookieProvider;
import com.example.laboratory.global.provider.JwtTokenProvider;
import com.example.laboratory.user.entity.User;
import com.example.laboratory.user.request.UserForm;
import com.example.laboratory.user.request.UserSession;
import com.example.laboratory.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CookieProvider cookieProvider;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<Object> userLogin(@RequestBody @Valid UserForm userForm) {
        Long userId = userService.userLogin(userForm);
        ResponseCookie cookie = cookieProvider.createCookie(jwtTokenProvider.createAccessToken(userId));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

}
