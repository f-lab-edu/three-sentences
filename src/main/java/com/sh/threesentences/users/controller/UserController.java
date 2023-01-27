package com.sh.threesentences.users.controller;

import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.service.UserService;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
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
    public UserResponseDto save(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.save(userRequestDto);
    }

    /**
     * 이메일 주소로 사용자 중복여부를 확인합니다.
     *
     * @param email 중복 확인 요청 이메일
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/duplicate-check")
    public void checkEmailDuplicated(@RequestParam @Email(message="이메일 형식이 잘못되었습니다.") String email) {
        userService.checkEmailDuplicated(email);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public UserResponseDto get(@PathVariable("id") Long id) {
        return userService.findUser(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }
}
