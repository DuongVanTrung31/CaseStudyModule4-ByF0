package cg.casestudy4f0.repository;

import cg.casestudy4f0.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ICategoryRepository extends PagingAndSortingRepository<Category, Long> {
    Page<Category> findAllByNameContaining(Category category,String name);
}
