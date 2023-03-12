package com.sh.threesentences.topic.controller;

import com.sh.threesentences.topic.dto.CommentRequestDto;
import com.sh.threesentences.topic.dto.CommentResponseDto;
import com.sh.threesentences.topic.service.CommentService;
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
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    public CommentResponseDto save(@RequestBody @Valid CommentRequestDto commentRequestDto) {
        return commentService.save(commentRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    public void delete(@PathVariable("id") Long id) {
        commentService.delete(id);
    }

}
