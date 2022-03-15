package cg.casestudy4f0.service;

import cg.casestudy4f0.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService {
    Page<Product> findAll(Pageable pageable);

    void save(Product product);

    void delete(Long id);

    Optional<Product> findById(Long id);

    Page<Product> findAllByName(Pageable pageable, String name);

    Page<Product> findByPriceBetween(Double min, Double max);

    Page<Product> findByNameAndPriceBetween(String name, Double min, Double max);

}
