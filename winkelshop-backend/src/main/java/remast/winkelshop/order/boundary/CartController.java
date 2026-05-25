package remast.winkelshop.order.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.order.control.CartService;
import remast.winkelshop.order.entity.Cart;
import remast.winkelshop.auth.boundary.User;

import java.util.UUID;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/items")
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<Cart> addItem(User user,
                                      @Valid @RequestBody AddCartItemRequest request) {
        cartService.addItem(user.userId(), request.productId(), request.quantity());
        return new DataResponse<>(cartService.getCart(user.userId()));
    }

    @GetMapping
    public DataResponse<Cart> getCart(User user) {
        return new DataResponse<>(cartService.getCart(user.userId()));
    }

    @PatchMapping("/items/{itemId}")
    public DataResponse<Cart> updateItem(User user,
                                         @PathVariable UUID itemId,
                                         @Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateItemQuantity(user.userId(), itemId, request.quantity());
        return new DataResponse<>(cartService.getCart(user.userId()));
    }

    @DeleteMapping("/items/{itemId}")
    public DataResponse<Cart> deleteItem(User user, @PathVariable UUID itemId) {
        cartService.removeItem(user.userId(), itemId);
        return new DataResponse<>(cartService.getCart(user.userId()));
    }

    public record AddCartItemRequest(UUID productId, @Min(1) int quantity) {
    }

    public record UpdateCartItemRequest(@Min(1) int quantity) {
    }

}
