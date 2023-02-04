package com.sh.threesentences.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_BLANK(BAD_REQUEST, "입력값이 필요합니다."),
    USER_NOT_FOUND(BAD_REQUEST, "사용자를 찾지 못했습니다."),
    EMAIL_DUPLICATE(BAD_REQUEST, "중복된 이메일이 존재합니다."),
    EMAIL_INVALID_FORMAT(BAD_REQUEST, "이메일 형식이 잘못되었습니다."),
    INVALID_VALUE_REQUEST(BAD_REQUEST, "입력 값이 유효하지 않습니다."),
    MEMBER_IS_STILL_IN_SPACE(BAD_REQUEST, "가입된 멤버가 있는 경우, ReadingSpace를 삭제할 수 없습니다."),
    NO_ADMIN_IN_SPACE(BAD_REQUEST, "해당 스페이스에 관리자가 존재하지 않습니다."),
    READING_SPACE_NOT_FOUND(BAD_REQUEST, "존재하지 않는 ReadingSpace 입니다.");

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
    private final HttpStatus httpStatus;
    private  final String message;
}
