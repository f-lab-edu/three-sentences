package com.sh.threesentences.topic.controller;

import com.sh.threesentences.auth.config.EmailAuthentication;
import com.sh.threesentences.topic.dto.TopicRequestDto;
import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.service.TopicService;
import java.util.List;
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
@RequestMapping("/api/v1/reading-spaces/{reading-space-id}/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TopicResponseDto save(@PathVariable("reading-space-id") Long readingSpaceId,
        @RequestBody @Valid TopicRequestDto topicRequestDto, EmailAuthentication auth) {
        return topicService.save(topicRequestDto, auth.getEmail(), readingSpaceId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{topic-id}")
    public TopicResponseDto getTopicInReadingSpace(@PathVariable("topic-id") Long topicId) {
        return topicService.getTopic(topicId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TopicResponseDto> getTopicsInReadingSpace(@PathVariable("reading-space-id") Long readingSpaceId,
        EmailAuthentication auth) {
        return topicService.getTopics(readingSpaceId);
    }
    
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{topic-id}")
    public void delete(@PathVariable("reading-space-id") Long readingSpaceId, @PathVariable("topic-id") Long topicId,
        EmailAuthentication auth) {
        topicService.delete(topicId, readingSpaceId, auth.getEmail());

    }


}
