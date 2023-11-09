package com.josh.gulfnet.controller;

import com.josh.gulfnet.model.JwtGeneratorInterface;
import com.josh.gulfnet.model.User;
import com.josh.gulfnet.service.JwtGeneratorImpl;
import com.josh.gulfnet.service.UserService;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private JwtGeneratorImpl jwtGenerator;

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable("userId") String userId){
       logger.info("Received request for userId");
        return new ResponseEntity<>(userService.getUserDetails(userId), HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@NotNull @RequestBody User user){
        logger.info("Received request for user creation, userId"+ user.getUserId());
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@NotNull @RequestBody User user){
        logger.info("Received request for login, userId"+ user.getUserId());
        User authenticatedUserData = userService.getUserByNameAndPassword(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(jwtGenerator.generateToken(authenticatedUserData), HttpStatus.OK);

    }
}
