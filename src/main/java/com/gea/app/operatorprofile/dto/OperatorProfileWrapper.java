package com.gea.app.operatorprofile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperatorProfileWrapper<T> {

    @JsonProperty("operator-profiles")
    private T operatorProfiles;

    public OperatorProfileWrapper(T operatorProfiles) {
        this.operatorProfiles = operatorProfiles;
    }
}