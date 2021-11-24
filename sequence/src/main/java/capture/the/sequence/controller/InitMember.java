package capture.the.sequence.controller;

import capture.the.sequence.model.UserCategory;
import capture.the.sequence.model.UserEntity;
import capture.the.sequence.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InitMember {

    private final InitMemberService initMemberService;

    @PostConstruct
    public void init() {
        initMemberService.init();
    }


    @Component
    static class InitMemberService {

        @Autowired
        private UserService userService;
        @Autowired
        private PasswordEncoder passwordEncoder;

        InitMemberService() {
        }

        @Transactional
        public void init() {

            UserEntity user = UserEntity.builder()
                    .email("yun@hello.com")
                    .username("yun")
                    .password(passwordEncoder.encode("1234"))
                    .created_at(LocalDateTime.now())
                    .approved(true)
                    .userCategory(UserCategory.ADMIN)
                    .build();
            userService.create(user);

            for (int i = 1; i <= 5; i++) {
                String email = "init" + i + "@hello.com";
                String username = "init_" + i;

                UserEntity insert = UserEntity.builder()
                        .email(email)
                        .username(username)
                        .password(passwordEncoder.encode("1234"))
                        .created_at(LocalDateTime.now())
                        .approved(i % 2 == 0 ? true : false)
                        .userCategory(UserCategory.GENERAL)
                        .build();
                userService.create(insert);
            }


        }
    }
}
