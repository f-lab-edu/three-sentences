package com.sh.threesentences.users.exception;

/**
 * 중복된 이메일로 등록하려는 경우 던집니다.
 */
public class EmailDuplicateException extends RuntimeException{
    public EmailDuplicateException() {
        super("이미 가입된 이메일 주소입니다.");
    }
}
