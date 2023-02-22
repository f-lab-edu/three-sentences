package com.sh.threesentences.topic.controller;

import com.sh.threesentences.auth.config.EmailAuthentication;
import com.sh.threesentences.topic.dto.TopicRequestDto;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.service.TopicService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reading-spaces/{id}/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TopicResponseDto save(@PathVariable("id") Long readingSpaceId,
        @RequestBody @Valid TopicRequestDto topicRequestDto, EmailAuthentication auth) {
        return topicService.save(topicRequestDto, auth.getEmail(), readingSpaceId);
    }
}
