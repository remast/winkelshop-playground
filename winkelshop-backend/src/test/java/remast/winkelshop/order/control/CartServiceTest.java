package remast.winkelshop.order.control;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import remast.winkelshop.order.entity.CartItemRepository;
import remast.winkelshop.order.entity.ProductRepository;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @InjectMocks
    private CartService cartService;

    @Test
    void addItem() {
        // TODO
    }

    @Test
    void shouldThrowExceptionWhenProductIsOutOfStock() {
        // TODO
    }
}