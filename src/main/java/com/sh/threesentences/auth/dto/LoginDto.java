package com.sh.threesentences.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginDto {

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    private final String password;

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
