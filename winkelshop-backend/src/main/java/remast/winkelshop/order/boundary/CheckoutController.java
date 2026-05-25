package remast.winkelshop.order.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.auth.boundary.User;
import remast.winkelshop.order.control.CheckoutService;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DataResponse<CheckoutService.CheckoutResult> checkout(User user,
                                                                 @Valid @RequestBody CheckoutRequest request) {
        var shippingAddress = request.shippingAddress();
        var result = checkoutService.checkout(
                user.userId(),
                request.paymentMethod(),
                shippingAddress.fullName(),
                shippingAddress.street(),
                shippingAddress.city(),
                shippingAddress.postalCode(),
                shippingAddress.country()
        );
        return new DataResponse<>(result);
    }

    public record CheckoutRequest(@Valid ShippingAddressRequest shippingAddress, @NotBlank String paymentMethod) {
    }

    public record ShippingAddressRequest(
            @NotBlank String fullName,
            @NotBlank String street,
            @NotBlank String city,
            @NotBlank String postalCode,
            @NotBlank String country
    ) {
    }
}
