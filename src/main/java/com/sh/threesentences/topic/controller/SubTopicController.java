package com.sh.threesentences.topic.controller;


import com.sh.threesentences.auth.config.EmailAuthentication;
import com.sh.threesentences.topic.dto.SubTopicRequestDto;
import com.sh.threesentences.topic.dto.SubTopicResponseDto;
import com.sh.threesentences.topic.service.SubTopicService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reading-spaces/{reading-space-id}/topic/{topic-id}/subtopics")
@RequiredArgsConstructor
public class SubTopicController {

    private final SubTopicService subTopicService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SubTopicResponseDto save(@PathVariable("reading-space-id") Long readingSpaceId,
        @PathVariable("topic-id") Long topicId,
        @RequestBody @Valid SubTopicRequestDto subTopicRequestDto, EmailAuthentication auth) {
        return subTopicService.save(subTopicRequestDto, auth.getEmail(), readingSpaceId, topicId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{subtopic-id}")
    public void delete(@PathVariable("reading-space-id") Long readingSpaceId,
        @PathVariable("subtopic-id") Long subTopicId, EmailAuthentication auth) {
        subTopicService.delete(readingSpaceId, subTopicId, auth.getEmail());

    }


}
