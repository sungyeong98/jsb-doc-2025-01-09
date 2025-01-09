package com.ll.sbbdoc20250109.domain.answer;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AnswerDto {

    private long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private long questionId;
    private long authorId;
    private String authorName;
    private String content;

    public AnswerDto(Answer answer) {
        this.id = answer.getId();
        this.createDate = answer.getCreateDate();
        this.modifyDate = answer.getModifyDate();
        this.questionId = answer.getQuestion().getId();
        this.authorId = answer.getUser().getId();
        this.authorName = answer.getUser().getNickname();
        this.content = answer.getContent();
    }

}
