package capture.the.sequence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
public class UserEntity {

    @Id
    @GeneratedValue(generator = "USER_GENERATOR")
    @GenericGenerator(name = "USER_GENERATOR", strategy = "uuid")
    @Column(name = "user_id")
    private String id; //유저에게 부여되는 고유 id
    private String username;
    private String email;
    private String password;
    private LocalDateTime created_at;
    private boolean approved;
    private UserCategory userCategory;
    @OneToMany(mappedBy = "user")
    private List<PriceEntity> priceEntityList = new ArrayList<>();
}
