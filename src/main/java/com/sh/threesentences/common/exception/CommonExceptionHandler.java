package com.sh.threesentences.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.sh.threesentences.common.dto.ErrorResponseDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponseDto handleIllegalStatesException(IllegalStateException ex) {
        return new ErrorResponseDto(BAD_REQUEST, ex.getMessage());
    }

    /**
     * 입력값이 ENUM에 포함되지 않는 경우, 예외를 던집니다.
     *
     * @return 메시지가 담긴 응답
     */
    @ExceptionHandler(InvalidFormatException.class)
    @ResponseStatus(code= BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValid() {
        return new ErrorResponseDto(ErrorCode.INVALID_VALUE_REQUEST.getHttpStatus(), ErrorCode.INVALID_VALUE_REQUEST.getMessage());
    }

}
