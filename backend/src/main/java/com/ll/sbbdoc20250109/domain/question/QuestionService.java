package com.ll.sbbdoc20250109.domain.question;

import com.ll.sbbdoc20250109.domain.user.SiteUser;
import com.ll.sbbdoc20250109.standard.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Optional<Question> findLatest() {
        return questionRepository.findFirstByOrderByIdDesc();
    }

    public Page<Question> findByListedPaged(boolean listed, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));

        return questionRepository.findByListed(listed, pageRequest);
    }

    public Page<Question> findByListedPaged(
            boolean listed,
            String searchKeywordType,
            String searchKeyword,
            int page,
            int pageSize
    ) {
        if (Ut.str.isBlank(searchKeyword)) return findByListedPaged(listed, page, pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));

        searchKeyword = "%" + searchKeyword + "%";

        return switch (searchKeywordType) {
            case "content" -> questionRepository.findByListedAndContentLike(listed, searchKeyword, pageRequest);
            default -> questionRepository.findByListedAndSubjectLike(listed, searchKeyword, pageRequest);
        };
    }

    public Page<Question> findByAuthorPaged(SiteUser author, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));

        return questionRepository.findByAuthor(author, pageRequest);
    }

    public Page<Question> findByAuthorPaged(
            SiteUser author,
            String searchKeywordType,
            String searchKeyword,
            int page,
            int pageSize
    ) {
        if (Ut.str.isBlank(searchKeyword)) return findByAuthorPaged(author, page, pageSize);

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));

        searchKeyword = "%" + searchKeyword + "%";

        return switch (searchKeywordType) {
            case "content" -> questionRepository.findByAuthorAndContentLike(author, searchKeyword, pageRequest);
            default -> questionRepository.findByAuthorAndSubjectLike(author, searchKeyword, pageRequest);
        };
    }

    public Question write(SiteUser user, String subject, String content, boolean published, boolean listed) {
        Question question = Question.builder()
                .author(user)
                .subject(subject)
                .content(content)
                .published(published)
                .listed(listed)
                .build();

        return questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void modify(Question question, String subject, String content, boolean published, boolean listed) {
        question.setSubject(subject);
        question.setContent(content);
        question.setPublished(published);
        question.setListed(listed);
    }

    public long count() {
        return questionRepository.count();
    }

    public List<Question> findAll() {
        return questionRepository.findAll();
    }

    public Optional<Question> findById(long id) {
        return questionRepository.findById(id);
    }

    public void flush() {
        questionRepository.flush();
    }

}
