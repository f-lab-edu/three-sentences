package com.sh.threesentences.readingspace.controller;

import com.sh.threesentences.readingspace.dto.ReadingSpaceRequestDto;
import com.sh.threesentences.readingspace.dto.ReadingSpaceResponseDto;
import com.sh.threesentences.readingspace.service.ReadingSpaceService;
import com.sh.threesentences.users.entity.User;
import com.sh.threesentences.users.service.UserService;
import java.util.List;
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
@RequestMapping("/api/v1/reading-spaces")
@RequiredArgsConstructor
public class ReadingSpaceController {

    private final ReadingSpaceService readingSpaceService;

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<ReadingSpaceResponseDto> getPublicReadingSpaces() {
        return readingSpaceService.getPublicReadingSpaces();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public List<ReadingSpaceResponseDto> getMyReadingSpaces() {
        return readingSpaceService.getMyReadingSpaces();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ReadingSpaceResponseDto createReadingSpace(@RequestBody ReadingSpaceRequestDto readingSpaceRequestDto) {

        // TODO: 임시 사용자 조회
        User tempUser = userService.findByIdTemp(1L);
        return readingSpaceService.create(readingSpaceRequestDto, tempUser);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/update")
    public ReadingSpaceResponseDto updateReadingSpace(@PathVariable("id") Long id,
        @RequestBody ReadingSpaceRequestDto readingSpaceRequestDto) {
        return readingSpaceService.update(readingSpaceRequestDto, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteReadingSpace(@PathVariable("id") Long id) {
        readingSpaceService.delete(id);
    }
}
