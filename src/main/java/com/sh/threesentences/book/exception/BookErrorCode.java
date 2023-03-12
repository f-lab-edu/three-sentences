package com.sh.threesentences.book.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BookErrorCode {

    BOOK_NOT_FOUND(BAD_REQUEST, "도서를 찾을 수 없습니다."),

    SEARCH_DUPLICATE_BOOK(BAD_REQUEST, "도서가 중복으로 검색되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    BookErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
