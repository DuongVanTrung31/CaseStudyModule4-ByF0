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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/home")
public class ProductController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    @ModelAttribute(name = "Category")
    private Iterable<Category> showCategory() {
        return iCategoryService.findAll();
    }

    @GetMapping
    public ModelAndView showProduct(@PageableDefault(value = 5) Pageable pageable, @RequestParam ) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        Page<Product> product = iProductService.findAll();
        modelAndView.addObject("product", product);
        return modelAndView;
    }
}
