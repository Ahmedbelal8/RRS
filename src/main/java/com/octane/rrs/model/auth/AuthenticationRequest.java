package com.octane.rrs.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class AuthenticationRequest {
    @JsonProperty("email")
    @NotBlank(message = "email required")
    @Email(message = "format not correct")
    private String email;
    @NotBlank(message = "password required")
    @JsonProperty("password")
    private String password;
}
