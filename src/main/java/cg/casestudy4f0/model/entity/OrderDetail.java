package cg.casestudy4f0.model.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "orders_id")
    private Order order;

    @ManyToOne
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;

    private int number;
}
