package cg.casestudy4f0.repository;

import cg.casestudy4f0.model.entity.CartItem;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> getCartItemByUser(User user);

    CartItem findByUserAndProduct(User user, Product product);

    @Query(value = "UPDATE CartItem c SET c.quantity = ?1 WHERE c.product.id = ?2 AND c.user.id = ?3")
    void updateQuantity(int quantity, Long productId, Long userId);

    void deleteByUser_IdAndProduct_Id(Long userId, Long productId);
}
