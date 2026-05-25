package remast.winkelshop.order.entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductRepository extends CrudRepository<ProductEntity, UUID>,
        PagingAndSortingRepository<ProductEntity, UUID> {

    Page<ProductEntity> findByCategoryId(UUID categoryId, Pageable pageable);
}
