package com.sh.threesentences.topic.controller;

import com.sh.threesentences.topic.dto.TopicResponseDto;
import com.sh.threesentences.topic.service.TopicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/topics")
@RequiredArgsConstructor
public class PublicTopicController {

    private final TopicService topicService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<TopicResponseDto> getPublicTopics(Pageable pageable) {
        return topicService.getPublicTopics(pageable);
    }

}
