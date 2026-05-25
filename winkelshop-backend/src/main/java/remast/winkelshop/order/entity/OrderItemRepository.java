package remast.winkelshop.order.entity;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends Repository<OrderItemEntity, UUID> {

    OrderItemEntity save(OrderItemEntity item);

    List<OrderItemEntity> findByOrderId(UUID orderId);

    @Modifying
    @Query("DELETE FROM order_items WHERE order_id = :orderId")
    int deleteByOrderId(@Param("orderId") UUID orderId);
}
