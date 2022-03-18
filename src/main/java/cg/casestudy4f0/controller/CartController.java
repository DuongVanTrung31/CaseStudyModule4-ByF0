package cg.casestudy4f0.controller;

import cg.casestudy4f0.model.CartModel;
import cg.casestudy4f0.model.entity.Order;
import cg.casestudy4f0.service.OrderService;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/demo")
public class CartController {
    private static final Logger LOG = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private OrderService orderService;




    //    thêm sản phẩm vào giỏ
    @PostMapping("api/cart")
    public ResponseEntity<?> addToCart(@RequestBody CartModel cartModel,HttpSession session) {
        Map<Long, CartModel> cart = (Map<Long, CartModel>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        if (cart.containsKey(cartModel.getProduct_id())) {
            CartModel cartModelCurrent = cart.get(cartModel.getProduct_id());
            cartModelCurrent.setQuantity(cartModelCurrent.getQuantity() + 1);
        } else {
            cart.put(cartModel.getProduct_id(), cartModel);
        }
        session.setAttribute("cart", cart);
        String[] key = {session.getId()};
        return new ResponseEntity<>(key, HttpStatus.OK);
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
    public ResponseEntity<?> showCartItems(HttpSession session) {
        Map<Long, CartModel> cart = (Map<Long, CartModel>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart",cart);
        }
        return new ResponseEntity<>(cart.values(), HttpStatus.OK);
    }

}
