package cg.casestudy4f0.repository;

import cg.casestudy4f0.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
