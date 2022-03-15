package cg.casestudy4f0.service;

import cg.casestudy4f0.model.CartModel;
import cg.casestudy4f0.model.entity.Order;

import java.util.Map;

public interface OrderService {
    void save(Order order);
    boolean addReceipt(Map<Long, CartModel> cart, Order order);
}
