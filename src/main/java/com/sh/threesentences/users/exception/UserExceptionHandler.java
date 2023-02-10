package com.sh.threesentences.users.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.sh.threesentences.common.dto.ErrorResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponseDto handleEmailDuplicateException(EmailDuplicateException ex) {
        return new ErrorResponseDto(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 등록되지 않은 사용자에 대한 접근을 할 때, 예외를 던집니다.
     *
     * @param ex UserNotFoundException
     * @return 메시지가 담긴 응답
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = BAD_REQUEST)
    public ErrorResponseDto handleUserNotFoundException(UserNotFoundException ex) {
        return new ErrorResponseDto(BAD_REQUEST, BAD_REQUEST.value(), ex.getMessage());
    }

    /**
     * 입력값이 Spring Validation 조건에 맞지 않는 경우, 예외를 던집니다.
     *
     * @param ex MethodArgumentNotValidException 예외
     * @return 메시지가 담긴 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code= BAD_REQUEST)
    public ErrorResponseDto handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList());

        return new ErrorResponseDto(BAD_REQUEST, BAD_REQUEST.value(), errors.toString());
    }
}
