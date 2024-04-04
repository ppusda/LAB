package com.example.laboratory.response;

import lombok.Builder;

@Builder
public record MemberLoginResponse(
        Long memberId,
        String username,
        String accessToken,
        String refreshToken
){
}
