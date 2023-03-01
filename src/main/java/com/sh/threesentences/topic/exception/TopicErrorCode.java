package com.sh.threesentences.topic.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum TopicErrorCode {

    UNAUTHORIZED_TO_CREATE_TOPIC(BAD_REQUEST, "스페이스 관리자만 토픽을 생성할 수 있습니다."),

    TOPIC_NOT_FOUND(BAD_REQUEST, "토픽이 존재하지 않습니다."),

    SUBTOPIC_NOT_FOUND(BAD_REQUEST, "서브 토픽이 존재하지 않습니다."),

    SENTENCE_NOT_FOUND(BAD_REQUEST, "문장이 존재하지 않습니다."),

    EXCEED_THE_NUMBER_OF_SENTENCES(BAD_REQUEST, "하나의 서브 토픽에 대해서 최대 3개의 문장만 입력이 가능합니다."),

    UNAUTHORIZED_TO_DELETE_SENTENCE(UNAUTHORIZED, "본인이 작성한 문장만 삭제가 가능합니다."),

    COMMENT_NOT_FOUND(BAD_REQUEST, "코멘트가 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    TopicErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
