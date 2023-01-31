package com.sh.threesentences.readingspace.controller;

import com.sh.threesentences.readingspace.entity.ReadingSpace;
import com.sh.threesentences.readingspace.service.ReadingSpaceService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/readingspaces")
@RequiredArgsConstructor
public class ReadingSpaceController {

    private final ReadingSpaceService readingSpaceService;


    /**
     * 애플리케이션 내의 공개된 전체 ReadingSpace를 조회한다.
     *
     * @return ReadingSpace 리스트
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("")
    public List<ReadingSpace> getPublicReadingSpaces() {
        return readingSpaceService.getPublicReadingSpaces();
    }
}
