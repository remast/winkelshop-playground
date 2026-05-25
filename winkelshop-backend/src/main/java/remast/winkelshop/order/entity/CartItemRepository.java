package remast.winkelshop.order.entity;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends Repository<CartItemEntity, UUID> {

    List<CartItemEntity> findByUserIdOrderByCreatedAtAsc(UUID userId);

    Optional<CartItemEntity> findByUserIdAndItemId(UUID userId, UUID itemId);

    CartItemEntity save(CartItemEntity item);

    void delete(CartItemEntity item);

    @Modifying
    @Query("DELETE FROM cart_items WHERE user_id = :userId")
    int deleteAllByUserId(@Param("userId") UUID userId);
}
