package com.sh.threesentences.topic.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TopicErrorCode {

    UNAUTHORIZED_TO_CREATE_TOPIC(BAD_REQUEST, "스페이스 관리자만 토픽을 생성할 수 있습니다."),

    TOPIC_NOT_FOUND(BAD_REQUEST, "삭제하려는 토픽이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TopicErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
