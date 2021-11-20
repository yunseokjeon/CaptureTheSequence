package capture.the.sequence.controller;

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
//                    .groups(Groups.GENERAL)
                    .build();
            userService.create(user);


        }
    }
}
