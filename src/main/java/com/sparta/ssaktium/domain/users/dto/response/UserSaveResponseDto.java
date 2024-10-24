package com.sparta.ssaktium.domain.users.dto.response;

import lombok.Getter;

@Getter
public class UserSaveResponseDto {

    private final String bearerToken;

    public UserSaveResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }
}
