package com.octane.rrs.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.octane.rrs.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @JsonProperty("name")
    @NotBlank(message = "name required")
    private String name;
    @JsonProperty("email")
    @NotBlank(message = "email required")
    @Email()
    private String email;
    @JsonProperty("password")
    @NotBlank(message = "password required")
    private String password;
    @JsonProperty("role")
    private Role role;
}
