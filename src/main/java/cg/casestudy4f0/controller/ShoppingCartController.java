package cg.casestudy4f0.controller;


import cg.casestudy4f0.model.entity.CartItem;
import cg.casestudy4f0.model.entity.User;
import cg.casestudy4f0.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class ShoppingCartController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/cart/{pid}/{qty}")
    public ResponseEntity<?> addItem(@PathVariable("qty") int qty,
                                     @PathVariable("pid") Long pid,
                                     @AuthenticationPrincipal Authentication auth) {
        Authentication authentication = authenticationManager.authenticate(auth);
        User user = (User) authentication.getPrincipal();
        cartItemService.addCartItem(qty,pid,user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("cart")
    public ResponseEntity<?> showCart(@AuthenticationPrincipal Authentication auth) {
        Authentication authentication = authenticationManager.authenticate(auth);
        User user = (User) authentication.getPrincipal();
        List<CartItem> cartItems = cartItemService.getCartItemByUser(user);
        return new ResponseEntity<>(cartItems,HttpStatus.OK);
    }
}
