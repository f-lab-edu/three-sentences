package com.sh.threesentences.utils;

import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.enums.MembershipType;

public class UserFixture {

    public static Long USER_ID = 1L;

    public static Long UNUSED_ID = 9999L;

    public static String VALID_NAME = "홍길동";

    public static String VALID_EMAIL = "abcd@gmail.com";

    public static String INVALID_EMAIL = "ejehejejgmail.com";

    public static String PASSWORD = "12345";

    public static MembershipType DEFAULT_MEMBERSHIP = MembershipType.FREE;

    public static final UserRequestDto VALID_REQUEST = new UserRequestDto
        (VALID_EMAIL, VALID_NAME, PASSWORD, DEFAULT_MEMBERSHIP);

    public static final UserRequestDto INVALID_REQUEST = new UserRequestDto
        (INVALID_EMAIL, VALID_NAME, PASSWORD, DEFAULT_MEMBERSHIP);

    public static final UserRequestDto INVALID_EMAIL_REQUEST = new UserRequestDto
        (INVALID_EMAIL, VALID_NAME, PASSWORD, DEFAULT_MEMBERSHIP);

    public static final UserResponseDto RESPONSE = new UserResponseDto(
        USER_ID, VALID_EMAIL, VALID_NAME, DEFAULT_MEMBERSHIP);

}
