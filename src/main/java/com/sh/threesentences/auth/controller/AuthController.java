package com.sh.threesentences.auth.controller;

import com.sh.threesentences.auth.dto.LoginDto;
import com.sh.threesentences.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("login")
    public void login(@RequestBody LoginDto loginDto) {
        authService.login(loginDto);
    }

}
