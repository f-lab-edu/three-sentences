package com.sh.threesentences.users.controller;

import com.sh.threesentences.users.dto.EmailDuplicateCheckDto;
import com.sh.threesentences.users.dto.UserRequestDto;
import com.sh.threesentences.users.dto.UserResponseDto;
import com.sh.threesentences.users.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public UserResponseDto save(@RequestBody @Valid UserRequestDto userRequestDto) {
        return userService.save(userRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/email/duplicate-check")
    public void checkEmailDuplicated(@RequestBody @Valid EmailDuplicateCheckDto emailDuplicateCheckDto) {
        userService.checkEmailDuplicated(emailDuplicateCheckDto.getEmail());
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
