package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<User> createUser(@NotNull @RequestBody User user){
//        log.info("Received request for user creation, userId"+ user.getUserId());
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }
}
