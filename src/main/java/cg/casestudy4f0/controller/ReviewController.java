package cg.casestudy4f0.controller;

import cg.casestudy4f0.model.entity.Review;
import cg.casestudy4f0.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired
    private ReviewService service;

//    Lấy ra tất cả review
    @GetMapping("/review")
    public ResponseEntity<List<Review>> showAllReviews() {
        List<Review> reviews = service.findAllCommentByIdDesc();
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

//    Đăng 1 reivew
    @PostMapping("/review")
    public ResponseEntity<Review> sendReview(@RequestBody Review review) {
        if (review.getContent() != null){
            service.saveReview(review);
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

//    Lấy review theo sản phẩm
    @GetMapping("/review/{id}")
    public ResponseEntity<List<Review>> showReviewsById(@PathVariable Long id) {
        return new ResponseEntity<>(service.findCommentsByIdProduct(id), HttpStatus.OK);
    }
}
