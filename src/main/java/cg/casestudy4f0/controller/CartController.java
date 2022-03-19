package cg.casestudy4f0.controller;

import cg.casestudy4f0.model.CartModel;
import cg.casestudy4f0.model.entity.Order;
import cg.casestudy4f0.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class CartController {
    private String key;
    @Autowired
    private OrderService orderService;

    //    thêm sản phẩm vào giỏ
    @PostMapping("/cart")
    public ResponseEntity<?> addToCart(@RequestBody CartModel cartModel, HttpSession session) {
        Map<Long, CartModel> cart = (Map<Long, CartModel>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        if (cart.containsKey(cartModel.getProduct_id())) {
            CartModel cartCurrent = cart.get(cartModel.getProduct_id());
            cartCurrent.setQuantity(cartCurrent.getQuantity() + 1);
        } else {
            cart.put(cartModel.getProduct_id(), cartModel);
        }
//        session.setMaxInactiveInterval(9999);
        session.setAttribute("cart", cart);
        key = session.getId();
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    //    Update sản phẩm trong giỏ
    @PutMapping("/cart")
    public ResponseEntity<Map<Long, CartModel>> updateCartItems(@RequestBody CartModel cartModel, HttpSession session) {
        Map<Long, CartModel> cart = (Map<Long, CartModel>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        if (cart.containsKey(cartModel.getProduct_id())) {
            CartModel cartCurrent = cart.get(cartModel.getProduct_id());
            cartCurrent.setQuantity(cartCurrent.getQuantity());
        } else {
            cart.put(cartModel.getProduct_id(), cartModel);
        }
        session.setAttribute("cart", cart);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    //    Xóa sản phẩm trong giỏ
    @DeleteMapping("/cart/{id}")
    public ResponseEntity<CartModel> deleteCartItem(@PathVariable(value = "id") Long id, HttpSession session) {
        Map<Long, CartModel> cart = (Map<Long, CartModel>) session.getAttribute("cart");
        CartModel c = new CartModel();
        if (cart != null && cart.containsKey(id)) {
            c = cart.get(id);
            cart.remove(id);
            session.setAttribute("cart", cart);
        }
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    //    Thanh toán
    @PostMapping("/pay")
    public HttpStatus pay(@RequestBody Order order, HttpSession session) {
        if (orderService.addReceipt((Map<Long, CartModel>) session.getAttribute("cart"), order)) {
            session.removeAttribute("cart");
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //    Hiển thị tất cả sản phẩm trong giỏ
    @GetMapping("/cart")
    public ResponseEntity<Map<Long, CartModel>> showCartItems(HttpSession session) {
        Map<Long, CartModel> cart = (Map<Long, CartModel>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
