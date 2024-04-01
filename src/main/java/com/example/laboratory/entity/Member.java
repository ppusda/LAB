package com.example.laboratory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "username은 필수 입력값입니다.")
    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String username;

    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String password;

    @Email
    private String email;

    @NotNull(message = "nickname은 필수 입력값입니다.")
    @Size(min = 5, max = 100)
    @Column(length = 100)
    private String nickname;

    private String imageUrl;

    @Builder.Default
    private Long credit = 0L;

    @Builder.Default
    private String provider = "COMMA";

    private String providerId;


}