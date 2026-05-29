package remast.winkelshop.order.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;

class CartTest {

    @Test
    void shouldCalculateLineTotalFromQuantityAndUnitPrice() {
        // Arrange
        var line = new Cart.CartItemLine(UUID.randomUUID(), UUID.randomUUID(), "Nimbus 2000", 3, 12.5);

        // Act
        var lineTotal = line.lineTotal();

        // Assert
        assertThat(lineTotal, closeTo(37.5, 0.0001));
    }

    @Test
    void shouldCalculateTotalPriceFromAllItemLines() {
        // Arrange
        var firstLine = new Cart.CartItemLine(UUID.randomUUID(), UUID.randomUUID(), "Nimbus 2000", 2, 10.5);
        var secondLine = new Cart.CartItemLine(UUID.randomUUID(), UUID.randomUUID(), "Firebolt", 1, 15.0);
        var cart = new Cart(List.of(firstLine, secondLine), Currency.GALLEON);

        // Act
        var totalPrice = cart.totalPrice();

        // Assert
        assertThat(totalPrice, closeTo(36.0, 0.0001));
    }

    @Test
    void shouldApplyBulkDiscountForFiveEqualProducts() {
        // Arrange
        var productId = UUID.randomUUID();
        var line = new Cart.CartItemLine(UUID.randomUUID(), productId, "Nimbus 2000", 5, 10.0);
        var cart = new Cart(List.of(line), Currency.GALLEON);

        // Act
        var discountAmount = cart.discountAmount();
        var totalPrice = cart.totalPrice();

        // Assert
        assertThat(discountAmount, closeTo(5.0, 0.0001));
        assertThat(totalPrice, closeTo(45.0, 0.0001));
    }

    @Test
    void shouldNotApplyBulkDiscountForFewerThanFiveEqualProducts() {
        // Arrange
        var productId = UUID.randomUUID();
        var line = new Cart.CartItemLine(UUID.randomUUID(), productId, "Nimbus 2000", 4, 10.0);
        var cart = new Cart(List.of(line), Currency.GALLEON);

        // Act
        var discountAmount = cart.discountAmount();
        var totalPrice = cart.totalPrice();

        // Assert
        assertThat(discountAmount, closeTo(0.0, 0.0001));
        assertThat(totalPrice, closeTo(40.0, 0.0001));
    }

    @Test
    void shouldApplyBulkDiscountAcrossMultipleLinesOfSameProduct() {
        // Arrange
        var productId = UUID.randomUUID();
        var firstLine = new Cart.CartItemLine(UUID.randomUUID(), productId, "Nimbus 2000", 2, 10.0);
        var secondLine = new Cart.CartItemLine(UUID.randomUUID(), productId, "Nimbus 2000", 3, 10.0);
        var otherLine = new Cart.CartItemLine(UUID.randomUUID(), UUID.randomUUID(), "Firebolt", 4, 20.0);
        var cart = new Cart(List.of(firstLine, secondLine, otherLine), Currency.GALLEON);

        // Act
        var discountAmount = cart.discountAmount();
        var totalPrice = cart.totalPrice();

        // Assert
        assertThat(discountAmount, closeTo(5.0, 0.0001));
        assertThat(totalPrice, closeTo(125.0, 0.0001));
    }
}
