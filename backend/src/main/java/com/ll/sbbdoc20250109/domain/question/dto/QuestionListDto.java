package com.ll.sbbdoc20250109.domain.question.dto;

import com.ll.sbbdoc20250109.domain.question.Question;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuestionListDto {

    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String subject;

    private String author;

    private boolean published;

    private boolean listed;

    public QuestionListDto(Question question) {
        this.id = question.getId();
        this.createDate = question.getCreateDate();
        this.modifyDate = question.getModifyDate();
        this.subject = question.getSubject();
        this.author = question.getAuthor().getNickname();
        this.published = question.isPublished();
        this.listed = question.isListed();
    }
}
