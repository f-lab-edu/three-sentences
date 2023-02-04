package com.sh.threesentences.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDto {

    /**
     * 예외 메시지
     */
    private HttpStatus httpStatus;

    private int httpStatusCode;

    private String errorMessage;

    public ErrorResponseDto(HttpStatus httpStatus, int httpStatusCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.httpStatusCode = httpStatusCode;
        this.errorMessage = errorMessage;
    }
}
