package com.ll.sbbdoc20250109.domain.question;

import com.ll.sbbdoc20250109.domain.question.dto.QuestionDetailDto;
import com.ll.sbbdoc20250109.domain.question.dto.QuestionListDto;
import com.ll.sbbdoc20250109.domain.user.SiteUser;
import com.ll.sbbdoc20250109.domain.user.UserService;
import com.ll.sbbdoc20250109.global.rq.Rq;
import com.ll.sbbdoc20250109.global.rsData.RsData;
import com.ll.sbbdoc20250109.standard.page.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question_list")
@RequiredArgsConstructor
@Tag(name = "QuestionController", description = "API 질문글 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final Rq rq;

    @Operation(summary = "글 목록 출력")
    @GetMapping
    @Transactional(readOnly = true)
    public PageDto<QuestionListDto> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "subject") String searchKeywordType,
            @RequestParam(defaultValue = "") String searchKeyword
    ) {
        return new PageDto<>(
                questionService.findByListedPaged(true, searchKeywordType, searchKeyword, page, pageSize)
                        .map(QuestionListDto::new)
        );
    }

    @Operation(summary = "글 상세보기")
    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public QuestionDetailDto getDetail(@PathVariable Long id) {
        Question question = questionService.findById(id).get();

        if (!question.isPublished()) {
            SiteUser user = rq.getActor();

            question.checkActorCanRead(user);
        }

        return new QuestionDetailDto(question);
    }

    record QuestionCreateReqBody (
            @NotBlank
            @Length(min = 3)
            String subject,
            @NotBlank
            @Length(min = 3)
            String content,
            boolean published,
            boolean listed
    ) {}

    @Operation(summary = "글 작성")
    @PostMapping
    @Transactional
    public RsData<QuestionDetailDto> createQuestion(
            @RequestBody @Valid QuestionCreateReqBody reqBody,
            @AuthenticationPrincipal UserDetails user
    ) {
        SiteUser actor = rq.findByActor().get();

        if (user != null) {
            actor = rq.getActorByUsername(user.getUsername());
        }

        Question question = questionService.write(actor, reqBody.subject, reqBody.content, reqBody.published, reqBody.listed);

        return new RsData<>(
                "201-1",
                "%d번 글이 작성되었습니다.".formatted(question.getId()),
                new QuestionDetailDto(question)
        );
    }

    record QuestionModifyReqBody (
            @NotBlank
            @Length(min = 3)
            String subject,
            @NotBlank
            @Length(min = 3)
            String content,
            boolean published,
            boolean listed
    ) {}

    @Operation(summary = "글 수정")
    @PutMapping("/{id}")
    @Transactional
    public RsData<QuestionDetailDto> modifyQuestion(@PathVariable Long id,
                                                    @RequestBody @Valid QuestionModifyReqBody reqBody) {
        SiteUser user = rq.getActor();

        Question question = questionService.findById(id).get();

        question.checkActorCanModify(user);

        questionService.modify(question, reqBody.subject, reqBody.content, reqBody.published, reqBody.listed);

        questionService.flush();

        return new RsData<>(
                "200-1",
                "%d번 글이 수정되었습니다.".formatted(id),
                new QuestionDetailDto(question)
        );
    }

    @Operation(summary = "글 삭제")
    @DeleteMapping("/{id}")
    @Transactional
    public RsData<Void> deleteQuestion(@PathVariable Long id) {
        SiteUser user = rq.getActor();

        Question question = questionService.findById(id).get();

        question.checkActorCanDelete(user);

        questionService.delete(question);

        return new RsData<>(
                "200-1",
                "%d번 글이 삭제되었습니다.".formatted(id)
        );
    }

    record QuestionStatisticsResBody(
            long totalQuestionCount,
            long totalPublishedCount,
            long totalListedCount
    ) {}

    @Operation(summary = "통계정보")
    @GetMapping("/statistics")
    @Transactional(readOnly = true)
    public QuestionStatisticsResBody questionStatistics() {
        SiteUser user = rq.getActor();

        return new QuestionStatisticsResBody(
                10,
                10,
                10
        );
    }

}