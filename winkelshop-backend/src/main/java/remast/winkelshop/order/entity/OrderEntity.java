package remast.winkelshop.order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("orders")
public record OrderEntity(
        @Id UUID id,
        @Column("user_id") UUID userId,
        String status,
        @Column("payment_method") String paymentMethod,
        double total,
        String currency,
        @Column("shipping_full_name") String shippingFullName,
        @Column("shipping_street") String shippingStreet,
        @Column("shipping_city") String shippingCity,
        @Column("shipping_postal_code") String shippingPostalCode,
        @Column("shipping_country") String shippingCountry,
        @Column("created_at") java.time.Instant createdAt
) {
}
