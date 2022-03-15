package cg.casestudy4f0.service.impl;

import cg.casestudy4f0.model.entity.OrderDetail;
import cg.casestudy4f0.repository.OrderDetailRepository;
import cg.casestudy4f0.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public void save(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
    }
}
