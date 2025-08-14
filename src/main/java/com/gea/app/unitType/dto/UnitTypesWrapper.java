package com.gea.app.unitType.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitTypesWrapper<T> {

    @JsonProperty("unit-types")
    private T unitTypes;

    public UnitTypesWrapper(T unitTypes) {
        this.unitTypes = unitTypes;
    }

}