package com.gea.app.helpRequest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HelpRequestWrapper<T> {

    @JsonProperty("help-requests")
    private T helpRequests;

    public HelpRequestWrapper(T helpRequests) {
        this.helpRequests = helpRequests;
    }
}