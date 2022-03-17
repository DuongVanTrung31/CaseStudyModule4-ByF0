package cg.casestudy4f0.repository;

import cg.casestudy4f0.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
