package cg.casestudy4f0.service;

import cg.casestudy4f0.model.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> findAllCommentByIdDesc();
    void saveReview(Review review);
    List<Review> findCommentsByIdProduct(Long id);
}
