package com.octane.rrs.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;

    public CustomException(String exceptionMessage, ErrorCode errorCode){
        super(exceptionMessage);
        this.errorCode=errorCode;
    }

    public CustomException(ErrorCode errorCode){
        super(errorCode.getErrorCodeDescription());
        this.errorCode=errorCode;
    }
}
