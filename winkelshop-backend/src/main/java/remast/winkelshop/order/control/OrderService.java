package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.order.boundary.PagedResponse;
import remast.winkelshop.order.entity.OrderEntity;
import remast.winkelshop.order.entity.OrderItemEntity;
import remast.winkelshop.order.entity.OrderItemRepository;
import remast.winkelshop.order.entity.OrderRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderDetails getOrder(UUID userId, UUID orderId) {
        var order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow();
        var items = orderItemRepository.findByOrderId(order.id());
        return new OrderDetails(order, items);
    }

    public PagedResponse<OrderEntity> getOrders(UUID userId, int page, int size) {
        var safePage = Math.max(page, 0);
        var safeSize = Math.max(size, 1);
        var offset = (long) safePage * safeSize;

        var content = orderRepository.findPageByUserId(userId, safeSize, offset);

        var total = orderRepository.countByUserId(userId);
        return PagedResponse.fromContentAndTotal(content, safePage, safeSize, total);
    }

    public record OrderDetails(OrderEntity order, java.util.List<OrderItemEntity> items) {
    }
}
