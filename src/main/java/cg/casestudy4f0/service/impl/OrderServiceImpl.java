package cg.casestudy4f0.service.impl;

import cg.casestudy4f0.model.CartModel;
import cg.casestudy4f0.model.entity.CartItem;
import cg.casestudy4f0.model.entity.Order;
import cg.casestudy4f0.model.entity.OrderDetail;
import cg.casestudy4f0.model.entity.Product;
import cg.casestudy4f0.repository.OrderDetailRepository;
import cg.casestudy4f0.repository.OrderRepository;
import cg.casestudy4f0.repository.ProductRepository;
import cg.casestudy4f0.service.OrderService;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void save(Order order) {
        orderRepository.save(order);
    }

    @Override
    public boolean addReceipt(List<CartItem> cart, Order order) {
        if(!cart.isEmpty()) {
            for (CartItem item:cart) {
                Product product = productRepository.findById(item.getProduct().getId()).get();
                orderDetailRepository.save(new OrderDetail(order,item.getProduct(),item.getQuantity()));
                product.setQuantity(product.getQuantity() - item.getQuantity());
            }
            return true;
        } else return false;
    }

}
