package cg.casestudy4f0.service;

import cg.casestudy4f0.model.entity.Category;

import java.util.Optional;

public interface ICategoryService {
    Iterable<Category> findAll();

    Optional<Category> findById(Long id);

    void save(Category category);
}
