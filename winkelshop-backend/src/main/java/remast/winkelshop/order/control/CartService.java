package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.stereotype.Service;
import remast.winkelshop.order.entity.Cart;
import remast.winkelshop.order.entity.CartItemEntity;
import remast.winkelshop.order.entity.CartItemRepository;
import remast.winkelshop.order.entity.Currency;
import remast.winkelshop.order.entity.ProductRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final JdbcAggregateTemplate jdbcAggregateTemplate;

    public CartItemEntity addItem(UUID userId, UUID productId, int quantity) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("product not found"));
        if (!product.inStock()) {
            throw new RuntimeException("product is out of stock");
        }

        return jdbcAggregateTemplate.insert(new CartItemEntity(
                UUID.randomUUID(),
                UUID.randomUUID(),
                userId,
                productId,
                quantity,
                product.price(),
                Instant.now()
        ));
    }

    public Cart getCart(UUID userId) {
        var lines = cartItemRepository.findByUserIdOrderByCreatedAtAsc(userId).stream()
                .map(item -> {
                    var product = productRepository.findById(item.productId())
                            .orElseThrow();
                    return new Cart.CartItemLine(item.itemId(), item.productId(), product.name(), item.quantity(), item.unitPrice());
                })
                .toList();

        return new Cart(lines, Currency.GALLEON);
    }

    public CartItemEntity updateItemQuantity(UUID userId, UUID itemId, int quantity) {
        var existing = cartItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow();
        return cartItemRepository.save(new CartItemEntity(
                existing.id(),
                existing.itemId(),
                existing.userId(),
                existing.productId(),
                quantity,
                existing.unitPrice(),
                existing.createdAt()
        ));
    }

    public void removeItem(UUID userId, UUID itemId) {
        var existing = cartItemRepository.findByUserIdAndItemId(userId, itemId)
                .orElseThrow();
        cartItemRepository.delete(existing);
    }

    public List<CartItemEntity> consumeCart(UUID userId) {
        var items = cartItemRepository.findByUserIdOrderByCreatedAtAsc(userId);
        cartItemRepository.deleteAllByUserId(userId);
        return items;
    }
}
