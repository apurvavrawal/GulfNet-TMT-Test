package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.ProfileResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.UserPostResponse;
import com.gulfnet.tmt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PatchMapping("/{userId}")
    @Operation(summary = "Update User Profile")
    public ResponseDto<UserPostResponse> updateProfile(@PathVariable UUID userId, @RequestParam(value = "profilePhoto", required = false) MultipartFile profilePhoto, @RequestParam(value = "languagePreference", required = false) String languagePreference) {
        log.info("Received user profile request for userId : {}, profilePhoto : {} and language : {} ", userId, profilePhoto, languagePreference);
        return userService.updateUserProfile(userId, profilePhoto, languagePreference);
    }

    @PutMapping(path = "/{userId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<UserPostResponse> updateUser(@PathVariable UUID userId, UserPostRequest user) {
        log.info("Received Request to update the user:{} with data:{}", userId, user);
        return userService.updateUser(userId, user);
    }

    @GetMapping
    public ResponseDto<UserPostResponse> getUsers(@RequestParam(value = "search", required = false) String search, @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received request for getting the users {}", search);
        return userService.getAllUsers(search, pageable);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get User Profile")
    public ResponseDto<ProfileResponse> getProfile(@RequestParam(value = "userName") String userName) {
        log.info("Received user get request for userName {}", userName);
        return userService.getProfile(userName);
    }
}
