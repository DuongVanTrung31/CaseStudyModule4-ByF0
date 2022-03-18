package cg.casestudy4f0.controller;

import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.service.CategoryService1;
import cg.casestudy4f0.service.ProductService1;
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
@RequestMapping("/product")
public class ProductController1 {
    @Autowired
    private ProductService1 productService1;

    @Autowired
    private CategoryService1 categoryService1;

    @ModelAttribute(name = "Category")
    private Iterable<Category> showCategory() {
        return categoryService1.findAll();
    }

    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping
    public ModelAndView showProduct(@PageableDefault(value = 5) Pageable pageable, @RequestParam Optional<String> search) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        Page<Product> products;
        if (search.isPresent()) {
            products = productService1.findAllByName(pageable, search.get());
            modelAndView.addObject("search", search.get());
        } else {
            products = productService1.findAll(pageable);
        }
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView showDetail(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("product/detail");
        Optional<Product> product = productService1.findById(id);
        product.ifPresent(value -> modelAndView.addObject("product", product));
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteProduct(@PageableDefault (value =  5) Pageable pageable, @PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        productService1.delete(id);
        Page<Product> products = productService1.findAll(pageable);
        modelAndView.addObject("products", products);
        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("product/create");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult, Model model) throws IOException {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("product", product);
            return "product/create";
        }
        MultipartFile multipartFile = product.getFile();
        String filename = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(product.getFile().getBytes(), new File(fileUpload + filename ));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        product.setEnsignUrl("image/" + filename);

        productService1.save(product);
        return "redirect:/product";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("product/edit");
        Optional<Product> product = productService1.findById(id);
        product.ifPresent(value -> modelAndView.addObject("product", value));
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String editProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult, Model model, @PathVariable("id") Long id) throws IOException {
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("product", product);
            return "product/edit";
        }

        MultipartFile multipartFile = product.getFile();
        String filename = multipartFile.getOriginalFilename();
        FileCopyUtils.copy(product.getEnsignUrl().getBytes(), new File(fileUpload + filename));
        product.setId(id);
        productService1.save(product);
        return "redirect:/product";
    }

    @PostMapping("/search-price")
    public ModelAndView searchPrice(@RequestParam("min") Double min, @RequestParam("max") Double max) {
        ModelAndView modelAndView = new ModelAndView("product/list");
        Page<Product> products = productService1.findByPriceBetween(min, max);
        modelAndView.addObject("products", products);
        return modelAndView;
    }
}
