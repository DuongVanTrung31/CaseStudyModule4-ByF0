package cg.casestudy4f0.controller;

import cg.casestudy4f0.model.entity.Order;
import cg.casestudy4f0.model.entity.User;
import cg.casestudy4f0.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/products")
@RestController
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> showAllUsers(){
        Iterable<User> Users = userService.getAllUsers();
        if (Users != null){
            return new ResponseEntity<>(Users, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Không có user nào", HttpStatus.OK);
        }
    }
}
