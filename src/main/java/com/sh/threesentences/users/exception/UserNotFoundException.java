package com.sh.threesentences.users.exception;

import com.sh.threesentences.common.exception.ErrorCode;

/**
 * 중복된 이메일로 등록하려는 경우 던집니다.
 */
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}
