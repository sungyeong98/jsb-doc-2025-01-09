package com.ll.sbbdoc20250109.domain.question;

import com.ll.sbbdoc20250109.domain.answer.Answer;
import com.ll.sbbdoc20250109.domain.base.BaseTime;
import com.ll.sbbdoc20250109.domain.user.SiteUser;
import com.ll.sbbdoc20250109.global.exceptions.ServiceException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Question extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser author;

    @Column(length = 100)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    // 공개 / 비공개
    private boolean published;

    // 검색 가능 / 불가능
    private boolean listed;

    @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST,CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Answer> answerList = new ArrayList<>();

    public Answer createAnswer(SiteUser user, String content) {
        Answer answer = Answer.builder()
                .question(this)
                .user(user)
                .content(content)
                .build();
        answerList.add(answer);

        return answer;
    }

    public List<Answer> getAnswerList() {
        return answerList.reversed();
    }

    public Optional<Answer> getAnswerById(Long id) {
        return answerList.stream().filter(answer -> answer.getId().equals(id)).findFirst();
    }

    public void deleteAnswer(Answer answer) {
        answerList.remove(answer);
    }

    public void checkActorCanDelete(SiteUser actor) {
        if (actor == null) throw new ServiceException("401-1", "로그인 후 이용해주세요.");

        if (actor.isAdmin()) return;

        if (actor.equals(author)) return;

        throw new ServiceException("403-1", "작성자만 글을 삭제할 수 있습니다.");
    }

    public void checkActorCanModify(SiteUser actor) {
        if (actor == null) throw new ServiceException("401-1", "로그인 후 이용해주세요.");

        if (actor.equals(author)) return;

        throw new ServiceException("403-1", "작성자만 글을 수정할 수 있습니다.");
    }

    public void checkActorCanRead(SiteUser actor) {
        if (actor == null) throw new ServiceException("401-1", "로그인 후 이용해주세요.");

        if (actor.isAdmin()) return;

        if (actor.equals(author)) return;

        throw new ServiceException("403-1", "비공개글은 작성자만 볼 수 있습니다.");
    }

}
