package com.ll.sbbdoc20250109.domain.base;

import com.ll.sbbdoc20250109.domain.question.Question;
import com.ll.sbbdoc20250109.domain.question.QuestionService;
import com.ll.sbbdoc20250109.domain.user.SiteUser;
import com.ll.sbbdoc20250109.domain.user.UserService;
import com.ll.sbbdoc20250109.global.app.AppConfig;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@RequiredArgsConstructor
public class BaseInitData {

    private final QuestionService questionService;
    private final UserService userService;

    @Autowired
    @Lazy
    private BaseInitData self;

    @Bean
    public ApplicationRunner baseInitDataApplicationRunner() {
        return args -> {
            self.work1();
            self.work2();
        };
    }

    @Transactional
    public void work1() {
        if (userService.count() > 0) return;

        SiteUser memberSystem = userService.join("system", "1234", "시스템", "system@system.com");
        if (AppConfig.isNotProd()) memberSystem.setApiKey("system");

        SiteUser memberAdmin = userService.join("admin", "1234", "관리자", "admin@admin.com");
        if (AppConfig.isNotProd()) memberAdmin.setApiKey("admin");

        SiteUser memberUser1 = userService.join("user1", "1234", "유저1", "user1@user.com");
        if (AppConfig.isNotProd()) memberUser1.setApiKey("user1");

        SiteUser memberUser2 = userService.join("user2", "1234", "유저2", "user2@user.com");
        if (AppConfig.isNotProd()) memberUser2.setApiKey("user2");

        SiteUser memberUser3 = userService.join("user3", "1234", "유저3", "user3@user.com");
        if (AppConfig.isNotProd()) memberUser3.setApiKey("user3");

        SiteUser memberUser4 = userService.join("user4", "1234", "유저4", "user4@user.com");
        if (AppConfig.isNotProd()) memberUser4.setApiKey("user4");

        SiteUser memberUser5 = userService.join("user5", "1234", "유저5", "user5@user.com");
        if (AppConfig.isNotProd()) memberUser5.setApiKey("user5");
    }

    @Transactional
    public void work2() {
        if (questionService.count() > 0) return;

        SiteUser memberUser1 = userService.findByUsername("user1").get();
        SiteUser memberUser2 = userService.findByUsername("user2").get();
        SiteUser memberUser3 = userService.findByUsername("user3").get();
        SiteUser memberUser4 = userService.findByUsername("user4").get();

        Question post1 = questionService.write(
                memberUser1,
                "축구 하실 분?",
                "14시 까지 22명을 모아야 합니다.",
                true,
                true
        );
        post1.createAnswer(memberUser2, "저요!");
        post1.createAnswer(memberUser3, "저도 할래요.");

        Question post2 = questionService.write(
                memberUser1,
                "배구 하실 분?",
                "15시 까지 12명을 모아야 합니다.",
                true,
                true
        );
        post2.createAnswer(memberUser4, "저요!, 저 배구 잘합니다.");

        Question post3 = questionService.write(
                memberUser2,
                "농구 하실 분?",
                "16시 까지 10명을 모아야 합니다.",
                true,
                true
        );

        Question post4 = questionService.write(
                memberUser3,
                "발야구 하실 분?",
                "17시 까지 14명을 모아야 합니다.",
                true,
                true
        );

        Question post5 = questionService.write(
                memberUser4,
                "피구 하실 분?",
                "18시 까지 18명을 모아야 합니다.",
                true,
                true
        );

        Question post6 = questionService.write(
                memberUser4,
                "발야구를 밤에 하실 분?",
                "22시 까지 18명을 모아야 합니다.",
                false,
                false
        );

        Question post7 = questionService.write(
                memberUser4,
                "발야구를 새벽 1시에 하실 분?",
                "새벽 1시 까지 17명을 모아야 합니다.",
                true,
                false
        );

        Question post8 = questionService.write(
                memberUser4,
                "발야구를 새벽 3시에 하실 분?",
                "새벽 3시 까지 19명을 모아야 합니다.",
                false,
                true
        );
    }

}
