package com.sh.threesentences.users.fixture;

import com.sh.threesentences.users.dto.EmailDuplicateCheckDto;
import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.enums.AuthorityType;
import com.sh.threesentences.users.enums.MembershipType;

public class UserFixture {

    public static Long USER_ID = 1L;

    public static Long UNUSED_ID = 9999L;

    public static String VALID_NAME = "홍길동";

    public static String VALID_EMAIL = "abcd@gmail.com";

    public static User USER = User.builder()
        .email(VALID_EMAIL)
        .name(VALID_NAME)
        .membership(MembershipType.FREE)
        .authorityType(AuthorityType.BASIC.getName())
        .build();
    
    public static String DUPLICATE_EMAIL = "abcd@gmail.com";

    public static String INVALID_EMAIL = "ejehejejgmail.com";

    public static String PASSWORD = "12345";

    public static String ENCRYPTED_PASSWORD = "1234512345";

    public static MembershipType DEFAULT_MEMBERSHIP = MembershipType.FREE;

    public static final UserRequestDto VALID_REQUEST = new UserRequestDto(VALID_EMAIL, VALID_NAME, PASSWORD,
        DEFAULT_MEMBERSHIP);

    public static final UserRequestDto INVALID_REQUEST = new UserRequestDto(INVALID_EMAIL, VALID_NAME, PASSWORD,
        DEFAULT_MEMBERSHIP);

    public static final UserRequestDto INVALID_EMAIL_REQUEST = new UserRequestDto(INVALID_EMAIL, VALID_NAME, PASSWORD,
        DEFAULT_MEMBERSHIP);

    public static final EmailDuplicateCheckDto VALID_EMAIL_DUPLICATE_CHECK_DTO = new EmailDuplicateCheckDto(
        VALID_EMAIL);

    public static final EmailDuplicateCheckDto EMAIL_DUPLICATE_CHECK_DTO = new EmailDuplicateCheckDto(DUPLICATE_EMAIL);

    public static final EmailDuplicateCheckDto INVALID_EMAIL_DUPLICATE_CHECK_DTO = new EmailDuplicateCheckDto(
        INVALID_EMAIL);
    public static final UserResponseDto RESPONSE = new UserResponseDto(USER_ID, VALID_EMAIL, VALID_NAME,
        DEFAULT_MEMBERSHIP);

    public static final UserRequestDto userRequestDtoWithInvalidNameOnly(String name) {
        return new UserRequestDto(VALID_EMAIL, name, "valid_password", MembershipType.BASIC);
    }

}
