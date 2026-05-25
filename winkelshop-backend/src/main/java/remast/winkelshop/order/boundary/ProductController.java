package remast.winkelshop.order.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.order.control.ProductService;
import remast.winkelshop.order.entity.ProductEntity;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public PagedResponse<ProductEntity> searchProducts(
            @RequestParam(name = "categoryId", required = false) UUID categoryId,
            @RequestParam(name = "category", required = false) UUID category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        var effectiveCategoryId = categoryId != null ? categoryId : category;
        return productService.searchProducts(effectiveCategoryId, page, size);
    }

    @GetMapping("/category/{categoryId}")
    public PagedResponse<ProductEntity> getProductsByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return productService.searchProducts(categoryId, page, size);
    }

    @GetMapping("/{productId}")
    public DataResponse<ProductEntity> getProduct(@PathVariable UUID productId) {
        return new DataResponse<>(productService.getProduct(productId));
    }

}
