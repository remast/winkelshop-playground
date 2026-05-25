package remast.winkelshop.auth.entity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends Repository<UserEntity, UUID> {

    @Query("SELECT * FROM users WHERE lower(email) = lower(:email)")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    Optional<UserEntity> findById(UUID id);
}
