package remast.winkelshop.order.entity;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends Repository<OrderEntity, UUID> {

    OrderEntity save(OrderEntity order);

    Optional<OrderEntity> findByIdAndUserId(UUID id, UUID userId);

    @Query("""
            SELECT * FROM orders
            WHERE user_id = :userId
            ORDER BY created_at DESC
            LIMIT :size OFFSET :offset
            """)
    List<OrderEntity> findPageByUserId(@Param("userId") UUID userId,
                                       @Param("size") int size,
                                       @Param("offset") long offset);

    long countByUserId(UUID userId);
}
