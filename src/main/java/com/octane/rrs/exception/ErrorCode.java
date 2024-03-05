package com.octane.rrs.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode{

    SERVER_ERROR("OC-001", "Unknown Technical error occurred.",HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_INPUT("OC-002", "The input data is invalid.",HttpStatus.BAD_REQUEST),
    NOT_FOUND("OC-003", "The requested data not found.",HttpStatus.NOT_FOUND),
    UNAUTHORIZED("OC-004","UnAuthorized",HttpStatus.UNAUTHORIZED),
    USER_EXIST("OC-005","This email already registered",HttpStatus.BAD_REQUEST),
    BAD_CREDENTIALS("OC-006","Bad Credentials",HttpStatus.UNAUTHORIZED),
    NOT_FOUND_USER("OC-007","Not Found User",HttpStatus.NOT_FOUND),
    NOT_FOUND_BOOK("OC-008","Not Found Book",HttpStatus.NOT_FOUND)
    ;
    private String code;
    private String errorCodeDescription;
    private HttpStatus httpStatus;
}
