package com.ll.sbbdoc20250109.domain.user;

import com.ll.sbbdoc20250109.domain.question.QuestionService;
import com.ll.sbbdoc20250109.domain.question.dto.QuestionListDto;
import com.ll.sbbdoc20250109.domain.user.dto.SiteUserDto;
import com.ll.sbbdoc20250109.global.exceptions.ServiceException;
import com.ll.sbbdoc20250109.global.rq.Rq;
import com.ll.sbbdoc20250109.global.rsData.RsData;
import com.ll.sbbdoc20250109.standard.page.PageDto;
import com.ll.sbbdoc20250109.standard.serach.SearchKeywordTypeV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "API 회원 컨트롤러")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final QuestionService questionService;
    private final UserService userService;
    private final Rq rq;
    private final AuthTokenService authTokenService;

    record UserSignupReqBody(
            @NotBlank
            @Length(min = 3, max = 20)
            String username,
            @NotBlank
            @Length(min = 3, max = 50)
            String password,
            @NotBlank
            @Length(min = 2, max = 20)
            String nickname,
            @NotBlank
            String email
    ) {}

    @Operation(summary = "회원가입")
    @PostMapping("/sign-up")
    @Transactional
    public RsData<SiteUserDto> signup(
            @Valid @RequestBody UserSignupReqBody reqBody
    ) {
        SiteUser user = userService.join(reqBody.username, reqBody.password, reqBody.nickname, reqBody.email);

        return new RsData<>(
                "201-1",
                "%s님, 환영합니다.".formatted(user.getNickname()),
                new SiteUserDto(user)
        );
    }

    record  UserLoginReqBody(
            @NotBlank
            String username,
            @NotBlank
            String password
    ) {}

    record UserLoginResBody(
            SiteUserDto item,
            String apiKey,
            String accessToken
    ) {}

    @Operation(summary = "로그인")
    @PostMapping("/login")
    @Transactional(readOnly = true)
    public RsData<UserLoginResBody> login(
            @Valid @RequestBody UserLoginReqBody reqBody
    ) {
        SiteUser user = userService
                .findByUsername(reqBody.username)
                .orElseThrow(() -> new ServiceException("409-1", "존재하지 않는 ID 입니다."));

        if(!user.matchPassword(reqBody.password)) {
            throw new ServiceException("401-2", "비밀번호가 일치하지 않습니다.");
        }

        String accessToken = userService.genAccessToken(user);

        rq.setCookie("accessToken", accessToken);
        rq.setCookie("apiKey", user.getApiKey());

        return new RsData<>(
                "200-1",
                "%s님, 환영합니다.".formatted(user.getNickname()),
                new UserLoginResBody(
                        new SiteUserDto(user),
                        user.getApiKey(),
                        accessToken
                )
        );
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("/logout")
    @Transactional(readOnly = true)
    public RsData<Void> logout() {
        rq.deleteCookie("accessToken");
        rq.deleteCookie("apiKey");

        return new RsData<>(
                "200-1",
                "로그아웃 되었습니다."
        );
    }

    @Operation(summary = "프로필")
    @GetMapping("/profile")
    @Transactional(readOnly = true)
    public SiteUserDto profile() {
        SiteUser user = rq.findByActor().get();

        return new SiteUserDto(user);
    }

    @Operation(summary = "내 글 조회")
    @GetMapping("/profile/my-list")
    @Transactional(readOnly = true)
    public PageDto<QuestionListDto> myList(
            @RequestParam(defaultValue = "title") SearchKeywordTypeV1 searchKeywordType,
            @RequestParam(defaultValue = "") String searchKeyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        SiteUser user = rq.getActor();

        return new PageDto<>(
                questionService.findByAuthorPaged(user, searchKeywordType, searchKeyword, page, pageSize)
                        .map(QuestionListDto::new)
        );
    }

}
