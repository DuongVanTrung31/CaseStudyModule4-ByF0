package cg.casestudy4f0.controller;


import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> showCategory() {
        Iterable<Category> categories = categoryService.findAll();
        if (!categories.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Optional<Category> category = categoryService.findById(id);
        if (!category.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        categoryService.remove(id);
        return new ResponseEntity<>(category.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Category> createProduct(@RequestBody Category category) {
        Category category1 = categoryService.save(category);
        return new ResponseEntity<>(category1, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Category> editProduct(@RequestBody Category category, @PathVariable("id") Long id) {
        Optional<Category> categoryOptional = categoryService.findById(id);
        if (!categoryOptional.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        category.setId(categoryOptional.get().getId());
        category = categoryService.save(category);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
