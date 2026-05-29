package remast.winkelshop.order.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

public record Cart(List<CartItemLine> items, Currency currency) {

    private static final int BULK_DISCOUNT_MIN_QUANTITY = 5;
    private static final double BULK_DISCOUNT_RATE = 0.10;

    public double subtotalPrice() {
        return items.stream().mapToDouble(CartItemLine::lineTotal).sum();
    }

    public double totalPrice() {
        return subtotalPrice() - discountAmount();
    }

    public double discountAmount() {
        return items.stream()
                .collect(Collectors.groupingBy(CartItemLine::productId))
                .values()
                .stream()
                .filter(lines -> lines.stream().mapToInt(CartItemLine::quantity).sum() >= BULK_DISCOUNT_MIN_QUANTITY)
                .mapToDouble(lines -> lines.stream().mapToDouble(CartItemLine::lineTotal).sum() * BULK_DISCOUNT_RATE)
                .sum();
    }

    public double getSubtotalPrice() {
        return subtotalPrice();
    }

    public double getDiscountAmount() {
        return discountAmount();
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
