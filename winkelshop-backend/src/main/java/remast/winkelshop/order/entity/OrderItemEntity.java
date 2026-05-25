package remast.winkelshop.order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("order_items")
public record OrderItemEntity(
        @Id @Column("id") UUID id,
        @Column("order_id") UUID orderId,
        @Column("product_id") UUID productId,
        String name,
        @Column("quantity") int quantity,
        @Column("unit_price") double unitPrice
) {
}
