package remast.winkelshop.auth.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("users")
public record UserEntity(
        @Id UUID id,
        String name,
        String email,
        String password,
        String role,
        @Column("created_at") java.time.Instant createdAt
) {
}
