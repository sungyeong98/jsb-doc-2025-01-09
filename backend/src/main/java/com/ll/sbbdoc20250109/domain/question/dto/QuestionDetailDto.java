package com.ll.sbbdoc20250109.domain.question.dto;

import com.ll.sbbdoc20250109.domain.question.Question;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class QuestionDetailDto {

    private long id;

    private String subject;

    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    private String author;

    private boolean published;

    private boolean listed;

    public QuestionDetailDto(Question question) {
        this.id = question.getId();
        this.subject = question.getSubject();
        this.content = question.getContent();
        this.createDate = question.getCreateDate();
        this.modifyDate = question.getModifyDate();
        this.author = question.getAuthor().getNickname();
        this.published = question.isPublished();
        this.listed = question.isListed();
    }

}
