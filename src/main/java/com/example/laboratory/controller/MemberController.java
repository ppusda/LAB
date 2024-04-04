package com.example.laboratory.controller;

import com.example.laboratory.request.MemberLoginRequest;
import com.example.laboratory.response.MemberLoginResponse;
import com.example.laboratory.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public MemberLoginResponse login(@RequestBody @Valid MemberLoginRequest memberLoginRequest) {
        return memberService.login(memberLoginRequest.username());
    }

}
