package cg.casestudy4f0.service;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService1 {
    Page<Product> findAll(Pageable pageable);

    void save(Product product);

    void delete(Long id);

    Optional<Product> findById(Long id);

    Page<Product> findAllByName(Pageable pageable, String name);

    Page<Product> findAllByCategory(Pageable pageable, Category category);

    Page<Product> findByPriceBetween(Double min, Double max);

    Page<Product> findByNameAndPriceBetween(String name, Double min, Double max);

}
