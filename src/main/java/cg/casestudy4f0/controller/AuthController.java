package cg.casestudy4f0.controller;

import cg.casestudy4f0.jwt.JwtService;
import cg.casestudy4f0.model.dto.LoginForm;
import cg.casestudy4f0.model.dto.SignUpForm;
import cg.casestudy4f0.model.entity.Role;
import cg.casestudy4f0.model.entity.User;
import cg.casestudy4f0.jwt.reponse.JwtResponse;
import cg.casestudy4f0.repository.RoleRepository;
import cg.casestudy4f0.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignUpForm signUpForm) {
        if(userService.existsByUsername(signUpForm.getUsername())){
            return new ResponseEntity<>(201,HttpStatus.CREATED);
        }
        User user = new User(signUpForm.getUsername(),passwordEncoder.encode(signUpForm.getPassword()),
                signUpForm.getEmail(),signUpForm.getFullName(),roleRepository.findByName(signUpForm.getRoleName()));
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.createToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User currentUser = userService.findByUsername(user.getUsername()).get();
        JwtResponse jwtResponse = new JwtResponse(currentUser.getId(),jwt,userDetails.getUsername(),currentUser.getFullName(),currentUser.getEmail(), userDetails.getAuthorities());
        return ResponseEntity.ok(jwtResponse);
    }

}
