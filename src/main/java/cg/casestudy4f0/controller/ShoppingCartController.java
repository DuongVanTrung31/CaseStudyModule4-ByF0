package cg.casestudy4f0.controller;


import cg.casestudy4f0.model.entity.CartItem;
import cg.casestudy4f0.model.entity.Order;
import cg.casestudy4f0.model.entity.User;
import cg.casestudy4f0.model.entity.enums.Status;
import cg.casestudy4f0.service.CartItemService;
import cg.casestudy4f0.service.OrderService;
import cg.casestudy4f0.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;


@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ShoppingCartController {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    UserService userService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/cart/{uid}/{pid}/{qty}")
    public ResponseEntity<?> addItem(@PathVariable("qty") int qty,
                                     @PathVariable("pid") Long pid,
                                     @PathVariable("uid") Long uid) {
        User user = userService.findById(uid).get();
        cartItemService.    addCartItem(qty,pid,user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("cart/{uid}")
    public ResponseEntity<?> showCart(@PathVariable("uid") Long uid) {
        User user = userService.findById(uid).get();
        List<CartItem> cartItems = cartItemService.getCartItemByUser(user);
        return new ResponseEntity<>(cartItems,HttpStatus.OK);
    }

    @PutMapping("cart/{uid}/{pid}/{qty}")
    public ResponseEntity<?> updateCart(@PathVariable("qty") int qty,
                                     @PathVariable("pid") Long pid,
                                     @PathVariable("uid") Long uid) {
        cartItemService.updateQuantity(qty,pid,uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("cart/{uid}/{pid}")
    public ResponseEntity<?> removeItem(@PathVariable("pid") Long pid,
                                        @PathVariable("uid") Long uid) {
        cartItemService.deleteByUserIdAndProductId(uid,pid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("cart/{uid}")
    public ResponseEntity<?> removeCart(@PathVariable("uid") Long uid) {
        cartItemService.removeByUserId(uid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/pay/{uid}")
    public ResponseEntity<?> pay(@RequestBody Order order,@PathVariable("uid")Long uid) {
        User user = userService.findById(uid).get();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        order.setUser(user);
        order.setCreatedAt(LocalDate.now());
        order.setStatus(Status.PENDING);
        simpleMailMessage.setTo(order.getEmail());
        simpleMailMessage.setSubject("Mail Tri Ân Khách Hàng");
        List<CartItem> cartItems = cartItemService.getCartItemByUser(user);
        boolean payCheck = orderService.addReceipt(cartItems,order);
        simpleMailMessage.setText("Cảm ơn quý khách đã mua hàng tại shop");
        simpleMailMessage.setText("Tên Khách Hàng : " +order.getFullName() +'\n'+
                                    "Số điện thoại : "+order.getPhone() +'\n'+
                                    "Địa chỉ : " + order.getAddress()+'\n'+
                                    "Tình trạng đơn hàng : "+order.getStatus().getStatus()+'\n'+
                                    "Ngày đặt hàng : "+order.getCreatedAt()+
                                    "Xin cảm ơn quý khách !!!");
        cartItemService.removeByUserId(uid);
        mailSender.send(simpleMailMessage);
        return new ResponseEntity<>(payCheck,HttpStatus.OK);
    }

}
