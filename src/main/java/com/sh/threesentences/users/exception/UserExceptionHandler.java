package com.sh.threesentences.users.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.sh.threesentences.common.dto.ErrorResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
    public ErrorResponseDto handleEmailDuplicateException() {
        return new ErrorResponseDto("이미 가입된 이메일입니다.");
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
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        return new ErrorResponseDto(processFieldErrors(fieldErrors));
    }

    private String processFieldErrors(List<FieldError> fieldErrors) {
        ArrayList<String> errorList = new ArrayList<>();
        for (org.springframework.validation.FieldError fieldError: fieldErrors) {
            errorList.add(fieldError.getDefaultMessage());
        }
        return errorList.toString();
    }

}
