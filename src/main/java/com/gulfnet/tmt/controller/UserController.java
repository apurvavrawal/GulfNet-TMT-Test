package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.UserPostResponse;
import com.gulfnet.tmt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Valid
public class UserController {
    private final UserService userService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "User Creation API")
    public ResponseDto<UserPostResponse> saveUser(UserPostRequest user) {
        log.info("Received user creation request for {}", user);
        return userService.saveUser(user);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "User Get API")
    public ResponseDto<UserPostResponse> getUser(@PathVariable UUID userId) {
        log.info("Received user get request for {}", userId);
        return userService.getUser(userId);
    }
}
