package cg.casestudy4f0.controller;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.service.ICategoryService;
import cg.casestudy4f0.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping(value =  "/category")
public class CategoryController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping
    public ModelAndView showCategory() {
        ModelAndView modelAndView = new ModelAndView("category/list");
        Iterable<Category> categories = iCategoryService.findAll();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/id")
    public ModelAndView showProductByCategory(@PageableDefault(value = 5)Pageable pageable, @PathVariable long id) {
        ModelAndView modelAndView = new ModelAndView("category/list-product");
        Page<Product> products;
        Optional<Category> category = iCategoryService.findById(id);
        if(category.isPresent()) {
            products = iProductService.findAllByCategory(pageable, category.get());
            modelAndView.addObject("category", category.get());
        } else {
            products = iProductService.findAll(pageable);
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView delete(@PageableDefault(value = 5) Pageable pageable, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("category/list");
        iCategoryService.delete(id);
        Iterable<Category> categories = iCategoryService.findAll();
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("category/create");
        modelAndView.addObject("category", new Category());
        return modelAndView;
    }

}
