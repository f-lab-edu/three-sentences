package com.sh.threesentences.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponseDto {

    /**
     * 예외 메시지
     */
    private HttpStatus httpStatus;
    private String errorMessage;

    public ErrorResponseDto(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }
}
