package cg.casestudy4f0.service.impl;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.repository.IProductRepository;
import cg.casestudy4f0.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ProductServiceImpl implements IProductService {
    @Autowired
    private IProductRepository iProductRepository;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return iProductRepository.findAll(pageable);
    }

    @Override
    public void save(Product product) {
        iProductRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        iProductRepository.deleteById(id);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return iProductRepository.findById(id);
    }

    @Override
    public Page<Product> findAllByName(Pageable pageable, String name) {
        return iProductRepository.findAllByNameContaining(pageable, name);
    }

    @Override
    public Page<Product> findAllByCategory(Pageable pageable, Category category) {
        return iProductRepository.findAllByCategory(pageable, category);
    }

    @Override
    public Page<Product> findByPriceBetween(Double min, Double max) {
        return iProductRepository.findAllByPriceBetween(min, max);
    }

    @Override
    public Page<Product> findByNameAndPriceBetween(String name, Double min, Double max) {
        return iProductRepository.findAllByNameContainingAndPriceBetween(name, min, max);
    }
}
