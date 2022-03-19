package cg.casestudy4f0.service.impl;


import cg.casestudy4f0.model.entity.CartItem;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.model.entity.User;
import cg.casestudy4f0.repository.CartItemRepository;
import cg.casestudy4f0.repository.ProductRepository;
import cg.casestudy4f0.repository.UserRepository;
import cg.casestudy4f0.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartRepo;

    @Autowired
    private ProductRepository productRepository;



    @Override
    public List<CartItem> getCartItemByUser(User user) {
        return cartRepo.getCartItemByUser(user);
    }

    public void addCartItem(int quantity,Long productId,User user) {
        Product product = productRepository.findById(productId).get();
        CartItem item = cartRepo.findByUserAndProduct(user,product);
        if(item != null) {
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item = new CartItem();
            item.setProduct(product);
            item.setUser(user);
            item.setQuantity(quantity);
        }
        cartRepo.save(item);
    }

    @Override
    public void updateQuantity(int quantity, Long productId, Long userId) {
        cartRepo.updateQuantity(quantity,productId,userId);
    }

    @Override
    public void deleteByUser_IdAndProduct_Id(Long userId, Long productId) {
        cartRepo.deleteByUser_IdAndProduct_Id(userId,productId);
    }
}
