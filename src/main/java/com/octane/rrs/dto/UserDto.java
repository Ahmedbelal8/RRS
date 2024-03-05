package com.octane.rrs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.octane.rrs.enums.Role;
import lombok.Data;

@Data
public class UserDto {
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("role")
    private Role role;
}
