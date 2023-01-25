package com.sh.threesentences.users.controller;

import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자를 등록 후 리턴합니다.
     *
     * @param userRequestDto 등록 요청 사용자 정보
     * @return 등록된 사용자 정보
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto save(@RequestBody UserRequestDto userRequestDto) {
        return userService.save(userRequestDto);
    }

}
