package com.sh.threesentences.users.exception;

import com.sh.threesentences.common.exception.ErrorCode;

/**
 * 중복된 이메일로 등록하려는 경우 던집니다.
 */
public class EmailDuplicateException extends RuntimeException{
    public EmailDuplicateException() {
        super(ErrorCode.EMAIL_DUPLICATE.getMessage());
    }
}
