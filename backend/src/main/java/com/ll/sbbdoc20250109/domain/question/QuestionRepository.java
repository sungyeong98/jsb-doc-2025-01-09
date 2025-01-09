package com.ll.sbbdoc20250109.domain.question;

import com.ll.sbbdoc20250109.domain.user.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findAllByOrderByIdDesc();

    Optional<Question> findFirstByOrderByIdDesc();

    Page<Question> findByListed(boolean listed, PageRequest pageRequest);

    Page<Question> findByListedAndSubjectLike(boolean listed, String subjectLike, PageRequest pageRequest);

    Page<Question> findByListedAndContentLike(boolean listed, String contentLike, PageRequest pageRequest);

    Page<Question> findByAuthor(SiteUser author, PageRequest pageRequest);

    Page<Question> findByAuthorAndSubjectLike(SiteUser author, String subjectLike, PageRequest pageRequest);

    Page<Question> findByAuthorAndContentLike(SiteUser author, String contentLike, PageRequest pageRequest);
}
