package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import remast.winkelshop.order.boundary.PagedResponse;
import remast.winkelshop.order.entity.ProductEntity;
import remast.winkelshop.order.entity.ProductRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public PagedResponse<ProductEntity> searchProducts(UUID categoryId,
                                                       int page,
                                                       int size) {
        var pageable = createPageRequest(page, size);
        var result = categoryId == null
                ? productRepository.findAll(pageable)
                : productRepository.findByCategoryId(categoryId, pageable);
        return toPagedResponse(result.getContent(), page, size, result.getTotalElements());
    }

    public PagedResponse<ProductEntity> getProductsByCategory(UUID categoryId,
                                                              int page,
                                                              int size) {
        return searchProducts(categoryId, page, size);
    }

    public ProductEntity getProduct(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow();
    }

    private static PageRequest createPageRequest(int page,
                                                 int size) {
        var safePage = Math.max(page, 0);
        var safeSize = Math.max(size, 1);
        return PageRequest.of(safePage, safeSize, Sort.by("name").ascending());
    }

    private static PagedResponse<ProductEntity> toPagedResponse(java.util.List<ProductEntity> content,
                                                                int page,
                                                                int size,
                                                                long totalElements) {
        var safePage = Math.max(page, 0);
        var safeSize = Math.max(size, 1);
        return PagedResponse.fromContentAndTotal(content, safePage, safeSize, totalElements);
    }
}
