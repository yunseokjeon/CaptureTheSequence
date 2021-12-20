package capture.the.sequence.controller;

import capture.the.sequence.dto.ResponseDTO;
import capture.the.sequence.dto.UserDTO;
import capture.the.sequence.model.UserCategory;
import capture.the.sequence.model.UserEntity;
import capture.the.sequence.security.TokenProvider;
import capture.the.sequence.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

//    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        try {

            UserEntity user = UserEntity.builder()
                    .email(userDTO.getEmail())
                    .username(userDTO.getUsername())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .created_at(LocalDateTime.now())
                    .approved(false)
                    .userCategory(UserCategory.GENERAL)
                    .build();

            UserEntity registeredUser = userService.create(user);

            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .created_at(registeredUser.getCreated_at())
                    .approved(registeredUser.isApproved())
                    .userCategory(registeredUser.getUserCategory())
                    .build();

            return ResponseEntity.ok(responseUserDTO);
        } catch (Exception e) {

            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(
                userDTO.getEmail(),
                userDTO.getPassword(),
                passwordEncoder);

        if (user != null && user.isApproved()) {
            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
                    .username(user.getUsername())
                    .approved(user.isApproved())
                    .userCategory(user.getUserCategory())
                    .email(user.getEmail())
                    .id(user.getId())
                    .token(token)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }

    @GetMapping("/getAllUerList")
    public ResponseEntity<?> getAllUserList(@AuthenticationPrincipal String userId) {
        System.out.println("userId = " + userId);
        System.out.println("user email  = " + userService.getUserById(userId).getEmail());

        if (userService.getUserById(userId).getUserCategory() != UserCategory.ADMIN) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("You are not ADMIN").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

        List<UserDTO> userList = userService.getAllUserList();

        ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(userList).build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/activateAccount")
    public ResponseEntity<?> activateAccount(@AuthenticationPrincipal String userId, @RequestBody UserDTO userDTO) {

        System.out.println("userId = " + userId + ", userDTO = " + userDTO);

        if (userService.getUserById(userId).getUserCategory() != UserCategory.ADMIN) {
            ResponseDTO responseDTO = ResponseDTO.builder().error("You are not ADMIN").build();
            return ResponseEntity.badRequest().body(responseDTO);
        }

        try {
            UserEntity userEntity = userService.activateAccount(userDTO.getEmail());

            UserDTO responseUserDTO = UserDTO.builder()
                    .email(userEntity.getEmail())
                    .id(userEntity.getId())
                    .username(userEntity.getUsername())
                    .created_at(userEntity.getCreated_at())
                    .approved(userEntity.isApproved())
                    .userCategory(userEntity.getUserCategory())
                    .build();

            return ResponseEntity.ok(responseUserDTO);
        } catch (Exception e) {

            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }

    }
}
