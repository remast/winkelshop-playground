package remast.winkelshop.order.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.order.entity.CategoryEntity;
import remast.winkelshop.order.entity.CategoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryEntity> getAllCategories() {
        return categoryRepository.findAll();
    }
}
