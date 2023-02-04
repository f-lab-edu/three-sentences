package com.sh.threesentences.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
    public ErrorResponseDto handleEmailDuplicateException(IllegalStateException ex) {
        return new ErrorResponseDto(ex.getMessage());
    }

}
