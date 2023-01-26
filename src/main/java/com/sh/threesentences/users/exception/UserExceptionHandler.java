package com.sh.threesentences.users.exception;

import com.sh.threesentences.common.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class UserExceptionHandler {

    /**
     * 이미 가입된 이메일로 등록을 요청할 경우, 예외를 던집니다.
     *
     * @return 메시지가 담긴 응답
     */
    @ExceptionHandler(EmailDuplicateException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleEmailDuplicateException() {
        return new ErrorResponseDto("이미 가입된 이메일입니다.");
    }
}
