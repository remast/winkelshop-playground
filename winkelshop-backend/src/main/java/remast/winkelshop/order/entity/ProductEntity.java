package remast.winkelshop.order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table("products")
public record ProductEntity(
        @Id UUID id,
        String name,
        String description,
        double price,
        String currency,
        @Column("in_stock") boolean inStock,
        int stock,
        @Column("category_id") UUID categoryId,
        @Column("image_url") String imageUrl
) {
}
