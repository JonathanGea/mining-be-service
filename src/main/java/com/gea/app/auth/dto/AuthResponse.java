package com.gea.app.auth.dto;

import lombok.Getter;

@Getter
public class AuthResponse {
    private final String accessToken;
    private final String tokenType;

    public AuthResponse(String accessToken, String tokenType) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
    }

    // Convenience ctor: default "Bearer"
    public AuthResponse(String accessToken) {
        this(accessToken, "Bearer");
    }

}
