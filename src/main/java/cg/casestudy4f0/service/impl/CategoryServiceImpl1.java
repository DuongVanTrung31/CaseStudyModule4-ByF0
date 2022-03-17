package cg.casestudy4f0.service.impl;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.repository.CategoryRepository1;
import cg.casestudy4f0.service.CategoryService1;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CategoryServiceImpl1 implements CategoryService1 {
    @Autowired
    private CategoryRepository1 iCategoryRepository;

    @Override
    public Iterable<Category> findAll() {
        return iCategoryRepository.findAll();
    }

    @Override
    public Optional<Category> findById(Long id) {
        return iCategoryRepository.findById(id);
    }

    @Override
    public void save(Category category) {
        iCategoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        iCategoryRepository.deleteById(id);
    }
}
