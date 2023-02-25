package com.sh.threesentences.topic.controller;

import com.sh.threesentences.auth.config.EmailAuthentication;
import com.sh.threesentences.topic.dto.SentenceRequestDto;
import com.sh.threesentences.topic.dto.SentenceResponseDto;
import com.sh.threesentences.topic.service.SentenceService;
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
@RequestMapping("/api/v1/reading-spaces/{reading-space-id}/topic/{topic-id}/subtopics/{subtopic-id}/sentences")
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public SentenceResponseDto save(@PathVariable("subtopic-id") Long subTopicId,
        @RequestBody @Valid SentenceRequestDto sentenceRequestDto, EmailAuthentication auth) {
        return sentenceService.save(sentenceRequestDto, auth.getEmail(), subTopicId);
    }

}
