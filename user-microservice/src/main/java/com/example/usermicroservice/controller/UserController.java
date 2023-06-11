package com.example.usermicroservice.controller;

import com.example.usermicroservice.jwt.JwtGeneratorInterface;
import com.example.usermicroservice.model.User;
import com.example.usermicroservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/users")
public class UserController {
    private UserService userService;
    private JwtGeneratorInterface jwtGenerator;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, JwtGeneratorInterface jwtGenerator, PasswordEncoder passwordEncoder){
        this.userService=userService;
        this.jwtGenerator=jwtGenerator;
        this.passwordEncoder=passwordEncoder;
    }
    @PostMapping("/register")
    public ResponseEntity<?> postUser(@RequestBody User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userService.saveUser(user);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            if (user.getUserName() == null || user.getPassword() == null) {
                throw new Exception("UserName or Password is Empty");
            }

            User userData = userService.getUserByName(user.getUserName());
            if (userData == null) {
                throw new Exception("UserName or Password is Invalid");
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(user.getPassword(), userData.getPassword())) {
                throw new Exception("UserName or Password is Invalid");
            }

            // Clear the password for security reasons
            userData.setPassword(null);

            return new ResponseEntity<>(jwtGenerator.generateToken(userData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

}