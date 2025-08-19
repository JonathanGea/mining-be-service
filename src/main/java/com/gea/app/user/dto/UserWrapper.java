package com.gea.app.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserWrapper<T> {

    @JsonProperty("users")
    private T user;

    public UserWrapper(T units) {
        this.user = units;
    }
}
