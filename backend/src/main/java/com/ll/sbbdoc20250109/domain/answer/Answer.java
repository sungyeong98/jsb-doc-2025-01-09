package com.ll.sbbdoc20250109.domain.answer;

import com.ll.sbbdoc20250109.domain.base.BaseTime;
import com.ll.sbbdoc20250109.domain.question.Question;
import com.ll.sbbdoc20250109.domain.user.SiteUser;
import com.ll.sbbdoc20250109.global.exceptions.ServiceException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Answer extends BaseTime {

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    private SiteUser user;

    @Column(columnDefinition = "TEXT")
    private String content;

    public void modify(String content) {
        this.content = content;
    }

    public void checkActorCanModify(SiteUser actor) {
        if (actor == null) {
            throw new ServiceException("401-1", "로그인 후 이용해주세요.");
        }
        if (actor.equals(user)) return;

        throw new ServiceException("403-2", "작성자만 수정 가능합니다.");
    }

    public void checkActorCanDelete(SiteUser actor) {
        if (actor == null) throw new ServiceException("401-1", "로그인 후 이용해주세요.");
        if (actor.equals(user)) return;
        if (actor.isAdmin()) return;
        throw new ServiceException("403-2", "작성자만 삭제 가능합니다.");
    }

}
