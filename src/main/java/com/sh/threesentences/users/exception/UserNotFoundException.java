package com.sh.threesentences.users.exception;

/**
 * 중복된 이메일로 등록하려는 경우 던집니다.
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("등록되지 않은 사용자입니다.");
    }
}
