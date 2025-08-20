package com.gea.app.auth.dto;

import com.gea.app.user.dto.UserResponse;
import lombok.Getter;

@Getter
public class AuthResponse {
    private final String accessToken;
    private final String tokenType;
    private final UserResponse user;


    public AuthResponse(String accessToken, String tokenType, UserResponse userResponse) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.user = userResponse;
    }

    public AuthResponse(String accessToken, UserResponse user) {
        this(accessToken, "Bearer", user);
    }
    public AuthResponse(String accessToken, String tokenType) {
        this(accessToken, tokenType, null);
    }
    public AuthResponse(String accessToken) {
        this(accessToken, "Bearer", null);
    }

}
