package cg.casestudy4f0.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartModel {
    private Long product_id;
    private String product_name;
    private int price;
    private int quantity;
}
