package com.gea.app.unit.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitWrapper<T> {

    @JsonProperty("units")
    private T units;

    public UnitWrapper(T units) {
        this.units = units;
    }
}