package remast.winkelshop.order.control;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import remast.winkelshop.order.entity.CartItemEntity;
import remast.winkelshop.order.entity.OrderItemRepository;
import remast.winkelshop.order.entity.OrderRepository;
import remast.winkelshop.order.entity.ProductEntity;
import remast.winkelshop.order.entity.ProductRepository;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private CartService cartControl;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CheckoutService checkoutService;

    @Test
    void shouldCheckoutCartAndCreateOrderWithItems() {
        // Arrange
        var userId = UUID.randomUUID();
        var broomId = UUID.randomUUID();
        var hatId = UUID.randomUUID();
        var cartItems = List.of(
                new CartItemEntity(UUID.randomUUID(), UUID.randomUUID(), userId, broomId, 2, 10.5, Instant.now()),
                new CartItemEntity(UUID.randomUUID(), UUID.randomUUID(), userId, hatId, 1, 15.0, Instant.now())
        );
        var broom = new ProductEntity(broomId, "Nimbus 2000", "Fast broom", 10.5, "GALLEON", true, 5, UUID.randomUUID(), "img");
        var hat = new ProductEntity(hatId, "Sorting Hat", "Magic hat", 15.0, "GALLEON", true, 3, UUID.randomUUID(), "img");

        when(cartControl.consumeCart(userId)).thenReturn(cartItems);
        when(productRepository.findById(broomId)).thenReturn(java.util.Optional.of(broom));
        when(productRepository.findById(hatId)).thenReturn(java.util.Optional.of(hat));
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderItemRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = checkoutService.checkout(
                userId,
                "credit-card",
                "Harry Potter",
                "4 Privet Drive",
                "Little Whinging",
                "CR3 0AA",
                "UK"
        );

        // Assert
        assertThat(result.order().id(), notNullValue());
        assertThat(result.order().userId(), equalTo(userId));
        assertThat(result.order().status(), equalTo("placed"));
        assertThat(result.order().paymentMethod(), equalTo("credit-card"));
        assertThat(result.order().total(), closeTo(36.0, 0.0001));
        assertThat(result.order().currency(), equalTo("GALLEON"));
        assertThat(result.order().shippingFullName(), equalTo("Harry Potter"));
        assertThat(result.order().shippingStreet(), equalTo("4 Privet Drive"));
        assertThat(result.order().shippingCity(), equalTo("Little Whinging"));
        assertThat(result.order().shippingPostalCode(), equalTo("CR3 0AA"));
        assertThat(result.order().shippingCountry(), equalTo("UK"));
        assertThat(result.order().createdAt(), notNullValue());

        assertThat(result.items(), hasSize(2));
        assertThat(result.items().stream().map(item -> item.name()).toList(), containsInAnyOrder("Nimbus 2000", "Sorting Hat"));
        assertThat(result.items().stream().map(item -> item.orderId()).distinct().toList(), containsInAnyOrder(result.order().id()));

        verify(cartControl).consumeCart(userId);
        verify(productRepository).findById(broomId);
        verify(productRepository).findById(hatId);
        verify(orderRepository).save(any());
        verify(orderItemRepository, times(2)).save(any());
    }

    @Test
    void shouldThrowWhenCartContainsUnknownProduct() {
        // Arrange
        var userId = UUID.randomUUID();
        var unknownProductId = UUID.randomUUID();
        var cartItems = List.of(
                new CartItemEntity(UUID.randomUUID(), UUID.randomUUID(), userId, unknownProductId, 1, 9.0, Instant.now())
        );

        when(cartControl.consumeCart(userId)).thenReturn(cartItems);
        when(productRepository.findById(unknownProductId)).thenReturn(java.util.Optional.empty());

        // Act
        var exception = assertThrows(NoSuchElementException.class, () -> checkoutService.checkout(
                userId,
                "credit-card",
                "Harry Potter",
                "4 Privet Drive",
                "Little Whinging",
                "CR3 0AA",
                "UK"
        ));

        // Assert
        assertThat(exception, notNullValue());
        verify(cartControl).consumeCart(userId);
        verify(productRepository).findById(unknownProductId);
        verify(orderRepository, never()).save(any());
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    void shouldCreateOrderWithZeroTotalWhenCartIsEmpty() {
        // Arrange
        var userId = UUID.randomUUID();
        when(cartControl.consumeCart(userId)).thenReturn(List.of());
        when(orderRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = checkoutService.checkout(
                userId,
                "invoice",
                "Hermione Granger",
                "Hampstead Garden Suburb",
                "London",
                "NW11",
                "UK"
        );

        // Assert
        assertThat(result.order().total(), closeTo(0.0, 0.0001));
        assertThat(result.items(), hasSize(0));
        verify(cartControl).consumeCart(userId);
        verify(productRepository, never()).findById(any());
        verify(orderRepository).save(any());
        verify(orderItemRepository, never()).save(any());
    }
}
