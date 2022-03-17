package cg.casestudy4f0.service;

import cg.casestudy4f0.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService extends GenericService<Product>{
    Page<Product> getAll(Pageable pageable);

    Iterable<Product> findAllByCategory(Long id);

    Iterable<Product> findByPriceBetween(int min, int max);

    Iterable<Product> findAllByNameContaining(String name);
}
