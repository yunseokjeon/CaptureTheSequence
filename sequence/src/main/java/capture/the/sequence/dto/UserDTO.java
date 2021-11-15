package capture.the.sequence.dto;

import capture.the.sequence.model.Groups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String token;
    private String username;
    private String email;
    private String password;
    private LocalDateTime created_at;
    private boolean approved;
//    private Groups groups;
    private String id;
}
