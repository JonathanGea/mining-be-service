package com.gea.app.adminUser.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminUsersWrapper<T> {

    @JsonProperty("user-admins")
    private T userAdmins;

    public AdminUsersWrapper(T userAdmins) {
        this.userAdmins = userAdmins;
    }

    public T getUserAdmins() {
        return userAdmins;
    }

    public void setUserAdmins(T userAdmins) {
        this.userAdmins = userAdmins;
    }
}