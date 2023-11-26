package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.UserPostResponse;
import com.gulfnet.tmt.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@Valid
public class UserController {
    private final UserService userService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<UserPostResponse> saveUser(UserPostRequest user) {
        log.info("Received user creation request for {}", user);
        return userService.saveUser(user);
    }
}
