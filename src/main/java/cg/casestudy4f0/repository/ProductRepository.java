package cg.casestudy4f0.repository;

import cg.casestudy4f0.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Iterable<Product> findAllByCategory_Id(Long id);
    Iterable<Product> findAllByNameContaining(String name);
    Iterable<Product> findAllByPriceBetween(int min, int max);
}
