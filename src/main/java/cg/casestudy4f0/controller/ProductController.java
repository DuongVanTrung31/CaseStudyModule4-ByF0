package cg.casestudy4f0.controller;


import cg.casestudy4f0.model.entity.Category;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/products")
public class ProductController {
    @Value("${upload.path}")
    private String upload;

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Iterable<Product>> showAll() {
        Iterable<Product> products = productService.findAll();
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id) {
        return new ResponseEntity<>(productService.findById(id),HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Product>> showAllPage(@PageableDefault(value = 4) Pageable pageable) {
        Page<Product> products = productService.getAll(pageable);
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> edit(@RequestPart("file")MultipartFile file,
                                     @RequestPart("product") Product product) {
        String fileName = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(upload + fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        product.setImage(file.getOriginalFilename());
        Category category = new Category();
        category.setId(product.getCategory().getId());
        product.setCategory(category);
        productService.save(product);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @PostMapping("/upload1")
    public ResponseEntity<?> upload1(@RequestPart("file")MultipartFile file,
                                          @RequestPart("product") Product product) {
        String fileName = file.getOriginalFilename();
        try {
            FileCopyUtils.copy(file.getBytes(), new File(upload + fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        product.setImage(file.getOriginalFilename());
        Category category = new Category();
        category.setId(product.getCategory().getId());
        product.setCategory(category);
        productService.save(product);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Product> delete(@PathVariable("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.remove(id);
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<?> showAllByName(@RequestParam("search") String search) {
        Iterable<Product> products = productService.findAllByNameContaining(search);
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }


    @GetMapping("/category/{id}")
    public ResponseEntity<Iterable<Product>> showByCate (@PathVariable("id") Long id) {
        Iterable<Product> products = productService.findAllByCategory(id);
        if (!products.iterator().hasNext()) {
            new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
