package remast.winkelshop.order.boundary;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.order.control.CategoryService;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public CategoryListResponse getCategories() {
        var categories = categoryService.getAllCategories();
        return new CategoryListResponse(categories, new CategoryListResponse.Meta(categories.size()));
    }
}
