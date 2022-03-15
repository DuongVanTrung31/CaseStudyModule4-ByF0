package cg.casestudy4f0.service.impl;

import cg.casestudy4f0.model.CartModel;
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
    public boolean addReceipt(Map<Long, CartModel> cart, Order order) {
        try {
            Order order1 = new Order();
            order1.setUser(order.getUser());
            order1.setCreatedAt(order.getCreatedAt());
            order1.setAddress(order.getAddress());
            order1.setFullName(order.getFullName());
            order1.setPhone(order.getPhone());
            order1.setEmail(order.getEmail());
            orderRepository.save(order1);
            for (CartModel cartModel : cart.values()){
                Product product = productRepository.findProductById(cartModel.getProduct_id());
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order1);
                orderDetail.setNumber(cartModel.getQuantity());
                orderDetail.setProduct(productRepository.findProductById(cartModel.getProduct_id()));
                product.setQuantity(product.getQuantity() - orderDetail.getNumber());
                orderDetailRepository.save(orderDetail);
            }
            return true;
        }
        catch (HibernateException e) {
            e.printStackTrace();
        }
        return false;
    }
}
