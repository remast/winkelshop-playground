package remast.winkelshop.order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("cart_items")
public record CartItemEntity(
        @Id @Column("id") UUID id,
        @Column("item_id") UUID itemId,
        @Column("user_id") UUID userId,
        @Column("product_id") UUID productId,
        @Column("quantity") int quantity,
        @Column("unit_price") double unitPrice,
        @Column("created_at") java.time.Instant createdAt
) {
}
