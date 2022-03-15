package cg.casestudy4f0.service.imp;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.repository.ICategoryRepository;
import cg.casestudy4f0.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository iCategoryRepository;

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
