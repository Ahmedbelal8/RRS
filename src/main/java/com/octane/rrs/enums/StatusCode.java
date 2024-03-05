package com.octane.rrs.enums;

public enum StatusCode {
    success,
    failed;

    public static StatusCode getStatusCode(boolean code) {
        return code ? success : failed;
    }
}
