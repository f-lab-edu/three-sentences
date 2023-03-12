package com.sh.threesentences.topic.controller;

import com.sh.threesentences.auth.config.EmailAuthentication;
import com.sh.threesentences.topic.dto.SentenceRequestDto;
import com.sh.threesentences.topic.dto.SentenceResponseDto;
import com.sh.threesentences.topic.service.SentenceService;
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
@RequestMapping("/api/v1/reading-spaces/{reading-space-id}/topic/{topic-id}")
@RequiredArgsConstructor
public class SentenceController {

    private final SentenceService sentenceService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("subtopics/{subtopic-id}/sentences")
    public SentenceResponseDto save(@PathVariable("subtopic-id") Long subTopicId,
        @RequestBody @Valid SentenceRequestDto sentenceRequestDto, EmailAuthentication auth) {
        return sentenceService.save(sentenceRequestDto, auth.getEmail(), subTopicId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("subtopics/{subtopic-id}/sentences")
    public List<SentenceResponseDto> getSentencesBySubTopic(@PathVariable("subtopic-id") Long subTopicId,
        EmailAuthentication auth) {
        return sentenceService.getSentencesBySubtopic(subTopicId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("subtopics/{subtopic-id}/sentences/me")
    public List<SentenceResponseDto> getMySentencesBySubTopic(@PathVariable("subtopic-id") Long subTopicId,
        EmailAuthentication auth) {
        return sentenceService.getMySentencesBySubTopic(subTopicId, auth.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("sentences")
    public List<SentenceResponseDto> getSentencesByTopic(@PathVariable("topic-id") Long topicId) {
        return sentenceService.getSentencesByTopic(topicId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("sentences/me")
    public List<SentenceResponseDto> getMySentencesByTopic(@PathVariable("topic-id") Long topicId,
        EmailAuthentication auth) {
        return sentenceService.getMySentencesByTopic(topicId, auth.getEmail());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("subtopics/{subtopic-id}/sentences/{sentence-id}")
    public void deleteSentence(@PathVariable("sentence-id") Long sentenceId, EmailAuthentication auth) {
        sentenceService.deleteSentence(sentenceId, auth.getEmail());
    }

}
