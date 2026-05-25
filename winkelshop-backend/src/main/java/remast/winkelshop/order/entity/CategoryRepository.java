package remast.winkelshop.order.entity;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends Repository<CategoryEntity, UUID> {
    List<CategoryEntity> findAll();
}
