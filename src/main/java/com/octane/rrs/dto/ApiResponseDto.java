package com.octane.rrs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.octane.rrs.enums.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    @JsonProperty("status_code")
    private StatusCode statusCode;
}
