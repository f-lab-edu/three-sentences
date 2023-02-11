package com.sh.threesentences.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 패스워드 암호화 작업을 처리합니다.
 */

// TODO: 확인해보고 삭제하기
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordEncoder {

    public static String encrypt(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeNoSuchAlgorithmException(e.getMessage());
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    static class RuntimeNoSuchAlgorithmException extends RuntimeException {

        public RuntimeNoSuchAlgorithmException(String message) {
            super(message);
        }
    }


}
