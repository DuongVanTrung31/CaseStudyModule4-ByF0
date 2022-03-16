package cg.casestudy4f0.controller;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.service.ICategoryService;
import cg.casestudy4f0.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping(value =  "/category")
public class CategoryController {
    @Autowired
    private IProductService iProductService;

    @Autowired
    private ICategoryService iCategoryService;

    @Value("${file-upload}")
    private String fileUpload;

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

    @PostMapping("/create")
    public String crateCategory(@Valid @ModelAttribute("category") Category category,
                                BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("category", category);
            return "category/create";
        }

        MultipartFile multipartFile = category.getFile();
        String filename = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(category.getFile().getBytes(), new File(fileUpload + filename ));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        category.setEnsignUrl("image/" + filename);
        iCategoryService.save(category);
        return "redirect:/category";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("category/edit");
        Optional<Category> category = iCategoryService.findById(id);
        category.ifPresent(value -> modelAndView.addObject("category", category));
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String editCategory(@Valid @ModelAttribute("category") Category category,
                               BindingResult bindingResult, Model model, @PathVariable("id") Long id) throws IOException {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("category", category);
            return "category/edit";
        }
        MultipartFile multipartFile = category.getFile();
        String filename = multipartFile.getOriginalFilename();
        FileCopyUtils.copy(category.getEnsignUrl().getBytes(), new File(fileUpload + filename));
        category.setId(id);
        iCategoryService.save(category);
        return "redirect:/category";
    }
}
