package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.User;
import com.gulfnet.tmt.service.JwtGeneratorImpl;
import com.gulfnet.tmt.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final JwtGeneratorImpl jwtGenerator;

    public UserController(UserService userService, JwtGeneratorImpl jwtGenerator) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
    }


    @GetMapping("/getUser/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable("userId") String userId){
       log.info("Received request for userId");
       return new ResponseEntity<>(userService.getUserDetails(userId), HttpStatus.OK);
    }

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@NotNull @RequestBody User user){
        log.info("Received request for user creation, userId"+ user.getUserId());
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@NotNull @RequestBody User user){
        log.info("Received request for login, userId"+ user.getUserId());
        User authenticatedUserData = userService.getUserByNameAndPassword(user.getUsername(), user.getPassword());
        return new ResponseEntity<>(jwtGenerator.generateToken(authenticatedUserData), HttpStatus.OK);

    }
}
