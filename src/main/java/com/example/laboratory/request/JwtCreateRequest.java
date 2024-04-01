package com.example.laboratory.request;

import lombok.Builder;

@Builder
public record JwtCreateRequest(
        long id,
        String username
){
}
