package cg.casestudy4f0.service;


import cg.casestudy4f0.model.entity.CartItem;
import cg.casestudy4f0.model.entity.Order;

import java.util.List;


public interface OrderService {
    void save(Order order);
    boolean addReceipt(List<CartItem> cart, Order order);
    Iterable<Order> getAllOrders();
}
