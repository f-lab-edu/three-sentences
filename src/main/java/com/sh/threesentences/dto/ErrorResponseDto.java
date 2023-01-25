package com.sh.threesentences.dto;

import lombok.Getter;

@Getter
public class ErrorResponseDto {

    /**
     * 예외 메시지
     */
    private String errorMessage;

    public ErrorResponseDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
