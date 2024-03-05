package com.octane.rrs.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Map;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDto {


    /**
     * The HTTP status code attached with the error
     * */
    private HttpStatus status;
    private String detail;
    private String reason;
    private String errorCode;
    private String errorCodeDescription;
    private Date timestamp;
    private Map<String, String> params;
}
