package com.ll.sbbdoc20250109.domain.answer;

import com.ll.sbbdoc20250109.domain.question.Question;
import com.ll.sbbdoc20250109.domain.question.QuestionService;
import com.ll.sbbdoc20250109.domain.user.SiteUser;
import com.ll.sbbdoc20250109.global.exceptions.ServiceException;
import com.ll.sbbdoc20250109.global.rq.Rq;
import com.ll.sbbdoc20250109.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question_list/{question_id}/answer_list")
public class AnswerController {

    private final QuestionService questionService;
    private final Rq rq;

    @GetMapping
    @Transactional(readOnly = true)
    public List<AnswerDto> getAnswers(@PathVariable Long question_id) {
        Question question = questionService.findById(question_id).orElseThrow(
                () -> new ServiceException("404-1", "%d번 글이 존재하지 않습니다.".formatted(question_id))
        );

        return question
                .getAnswerList()
                .stream()
                .map(AnswerDto::new)
                .toList();
    }

    record AnswerCreateReqbody(
            @NotBlank
            @Length(min = 1, max = 100)
            String content
    ) {}

    @PostMapping
    @Transactional
    public RsData<AnswerDto> createAnswer(@PathVariable Long question_id,
                                          @Valid @RequestBody AnswerCreateReqbody reqbody) {
        SiteUser user = rq.getActor();

        Question question = questionService.findById(question_id).orElseThrow(
                () -> new ServiceException("404-1", "%d번 글이 존재하지 않습니다.".formatted(question_id))
        );

        Answer answer = question.createAnswer(
                user,
                reqbody.content
        );

        questionService.flush();

        return new RsData<>(
                "201-1",
                "%d번 댓글이 작성되었습니다.".formatted(answer.getId()),
                new AnswerDto(answer)
        );
    }

    record AnswerModifyReqbody(
            @NotBlank
            @Length(min = 1, max = 100)
            String content
    ) {}

    @PutMapping("/{id}")
    @Transactional
    public RsData<AnswerDto> modifyAnswer(@PathVariable Long question_id,
                                          @PathVariable Long id,
                                          @Valid @RequestBody AnswerModifyReqbody reqbody) {
        SiteUser user = rq.getActor();

        Question question = questionService.findById(question_id).orElseThrow(
                () -> new ServiceException("404-1", "%d번 글이 존재하지 않습니다.".formatted(question_id))
        );

        Answer answer = question.getAnswerById(id).orElseThrow(
                () -> new ServiceException("404-2", "%d번 댓글이 존재하지 않습니다.".formatted(id))
        );

        answer.checkActorCanModify(user);

        answer.modify(reqbody.content);

        return new RsData<>(
                "200-1",
                "%d번 댓글이 수정되었습니다.".formatted(answer.getId()),
                new AnswerDto(answer)
        );
    }

    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> deleteAnswer(@PathVariable Long question_id, @PathVariable Long id) {
        SiteUser user = rq.getActor();

        Question question = questionService.findById(question_id).orElseThrow(
                () -> new ServiceException("404-1", "%d번 글이 존재하지 않습니다.".formatted(question_id))
        );

        Answer answer = question.getAnswerById(id).orElseThrow(
                () -> new ServiceException("404-2", "%d번 댓글이 존재하지 않습니다.".formatted(id))
        );

        answer.checkActorCanDelete(user);

        question.deleteAnswer(answer);

        return new RsData<>(
                "200-1",
                "%d번 댓글이 삭제되었습니다.".formatted(id)
        );

    }

}
