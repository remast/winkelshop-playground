package remast.winkelshop.order.entity;

import java.util.List;
import java.util.UUID;

public record Cart(List<CartItemLine> items, Currency currency) {

    public double totalPrice() {
        return items.stream().mapToDouble(CartItemLine::lineTotal).sum();
    }

    public double getTotalPrice() {
        return totalPrice();
    }

    public record CartItemLine(UUID itemId, UUID productId, String name, int quantity, double unitPrice) {
        public double lineTotal() {
            return unitPrice * quantity;
        }
    }
}
