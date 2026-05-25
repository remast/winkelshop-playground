package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import remast.winkelshop.order.entity.Currency;
import remast.winkelshop.order.entity.OrderEntity;
import remast.winkelshop.order.entity.OrderItemEntity;
import remast.winkelshop.order.entity.OrderItemRepository;
import remast.winkelshop.order.entity.OrderRepository;
import remast.winkelshop.order.entity.ProductEntity;
import remast.winkelshop.order.entity.ProductRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CartService cartControl;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CheckoutResult checkout(UUID userId,
                                   String paymentMethod,
                                   String shippingFullName,
                                   String shippingStreet,
                                   String shippingCity,
                                   String shippingPostalCode,
                                   String shippingCountry) {
        var cartItems = cartControl.consumeCart(userId);
        var products = new HashMap<UUID, ProductEntity>();
        var total = cartItems.stream()
                .map(item -> {
                    var product = productRepository.findById(item.productId())
                            .orElseThrow();
                    products.put(item.productId(), product);
                    return item.unitPrice() * item.quantity();
                })
                .mapToDouble(Double::doubleValue)
                .sum();
        var orderId = UUID.randomUUID();
        var createdAt = Instant.now();

        var orderEntity = orderRepository.save(new OrderEntity(
                orderId,
                userId,
                "placed",
                paymentMethod,
                total,
                Currency.GALLEON.name(),
                shippingFullName,
                shippingStreet,
                shippingCity,
                shippingPostalCode,
                shippingCountry,
                createdAt
        ));

        var savedItems = cartItems.stream()
                .map(item -> orderItemRepository.save(new OrderItemEntity(
                        UUID.randomUUID(),
                        orderEntity.id(),
                        item.productId(),
                        products.get(item.productId()).name(),
                        item.quantity(),
                        item.unitPrice()
                )))
                .toList();

        return new CheckoutResult(orderEntity, savedItems);
    }

    public record CheckoutResult(OrderEntity order, List<OrderItemEntity> items) {
    }
}
