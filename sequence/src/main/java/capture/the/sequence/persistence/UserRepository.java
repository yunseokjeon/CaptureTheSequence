package capture.the.sequence.persistence;

import capture.the.sequence.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);

    Boolean existsByEmail(String email);

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity getById(String id);

    @Query("select m from UserEntity m")
    List<UserEntity> findAllUserList();
}
