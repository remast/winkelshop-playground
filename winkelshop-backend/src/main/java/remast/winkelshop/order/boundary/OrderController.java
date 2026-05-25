package remast.winkelshop.order.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.auth.boundary.User;
import remast.winkelshop.order.control.OrderService;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{orderId}")
    public DataResponse<OrderService.OrderDetails> getOrder(User user, @PathVariable UUID orderId) {
        return new DataResponse<>(orderService.getOrder(user.userId(), orderId));
    }

    @GetMapping
    public PagedResponse<OrderHistoryItem> getOrders(User user,
                                                     @RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        return orderService.getOrders(user.userId(), page, size)
                .map(order -> new OrderHistoryItem(
                        order.id(),
                        order.status(),
                        order.total(),
                        order.currency(),
                        order.createdAt()
                ));
    }

    public record OrderHistoryItem(UUID orderId, String status, double total, String currency, Instant createdAt) {
    }
}
