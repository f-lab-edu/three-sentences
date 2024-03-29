package com.sh.threesentences.readingspace.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import org.springframework.http.HttpStatus;

public enum ReadingSpaceErrorCode {

    MEMBER_IS_STILL_IN_SPACE(BAD_REQUEST, "가입된 멤버가 있는 경우, ReadingSpace를 삭제할 수 없습니다."),
    NO_ADMIN_IN_SPACE(BAD_REQUEST, "해당 스페이스에 관리자가 존재하지 않습니다."),
    READING_SPACE_NOT_FOUND(BAD_REQUEST, "존재하지 않는 ReadingSpace 입니다."),
    DELETE_ADMIN_ONLY(FORBIDDEN, "관리자만 스페이스를 삭제할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ReadingSpaceErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
