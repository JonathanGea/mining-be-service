package com.gea.app.assignment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssignmentWrapper<T> {

    @JsonProperty("assignments")
    private T assignments;

    public AssignmentWrapper(T assignments) {
        this.assignments = assignments;
    }
}