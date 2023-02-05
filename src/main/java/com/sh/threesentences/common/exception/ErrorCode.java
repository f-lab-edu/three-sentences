package com.sh.threesentences.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_BLANK(BAD_REQUEST, "입력값이 필요합니다."),
    INVALID_VALUE_REQUEST(BAD_REQUEST, "입력 값이 유효하지 않습니다.");

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
    private final HttpStatus httpStatus;
    private  final String message;
}
