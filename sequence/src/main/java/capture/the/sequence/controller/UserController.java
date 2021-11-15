package capture.the.sequence.controller;

import capture.the.sequence.dto.ResponseDTO;
import capture.the.sequence.dto.UserDTO;
import capture.the.sequence.model.Groups;
import capture.the.sequence.model.UserEntity;
import capture.the.sequence.security.TokenProvider;
import capture.the.sequence.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
//                    .groups(Groups.GENERAL)
                    .build();

            UserEntity registeredUser = userService.create(user);

            UserDTO responseUserDTO = UserDTO.builder()
                    .email(registeredUser.getEmail())
                    .id(registeredUser.getId())
                    .username(registeredUser.getUsername())
                    .created_at(registeredUser.getCreated_at())
                    .approved(registeredUser.isApproved())
//                    .groups(registeredUser.getGroups())
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

        if (user != null) {
            // 토큰 생성
            final String token = tokenProvider.create(user);
            final UserDTO responseUserDTO = UserDTO.builder()
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
        System.out.println("user email  = " + userService.getUserByEmail(userId).getEmail());

        List<UserDTO> userList = userService.getAllUserList();

        ResponseDTO<UserDTO> response = ResponseDTO.<UserDTO>builder().data(userList).build();
        return ResponseEntity.ok(response);

    }

}
