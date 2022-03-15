package cg.casestudy4f0.repository;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IProductRepository extends PagingAndSortingRepository<Product, Long> {
    Page<Product> findAllByNameContaining(Pageable pageable, String name);

    Page<Product> findAllByPriceBetween(Double min, Double max);

    Page<Product> findAllByNameContainingAndPriceBetween(String name, Double min, Double max);

    Page<Product> findAllByCategory(Pageable pageable, Category category);
}
