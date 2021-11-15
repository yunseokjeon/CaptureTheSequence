package capture.the.sequence.service;

import capture.the.sequence.dto.UserDTO;
import capture.the.sequence.model.UserEntity;
import capture.the.sequence.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity create(final UserEntity userEntity) {
        if(userEntity == null || userEntity.getEmail() == null ) {
            throw new RuntimeException("Invalid arguments");
        }
        final String email = userEntity.getEmail();
        if(userRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("Email already exists");
        }

        return userRepository.save(userEntity);
    }

    public UserEntity getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final UserEntity originalUser = userRepository.findByEmail(email);

        // matches 메서드를 이용해 패스워드가 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            return originalUser;
        }
        return null;
    }

    public UserEntity getUserByEmail(String id){
        return userRepository.getById(id);
    }

    public List<UserDTO> getAllUserList(){
        List<UserEntity> allUserList = userRepository.findAllUserList();

        List<UserDTO> userDtoList = new ArrayList<>();
        for (UserEntity userEntity : allUserList) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(userEntity.getUsername());
            userDTO.setEmail(userEntity.getEmail());
            userDTO.setCreated_at(userEntity.getCreated_at());
            userDTO.setApproved(userEntity.isApproved());
            userDTO.setId(userEntity.getId());
            userDtoList.add(userDTO);
        }

        return userDtoList;
    }

}
