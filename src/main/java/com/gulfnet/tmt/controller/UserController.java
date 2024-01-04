package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.UserContactsRequest;
import com.gulfnet.tmt.model.request.UserFilterRequest;
import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.ProfileResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.UserContactsResponse;
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
    public ResponseDto<UserPostResponse> updateProfile(@PathVariable UUID userId, @RequestParam(value = "languagePreference", required = false) String languagePreference) {
        log.info("Received user profile request for userId : {} and language : {} ", userId, languagePreference);
        return userService.updateUserProfile(userId , languagePreference);
    }

    @PutMapping(path = "/{userId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Update User")
    public ResponseDto<UserPostResponse> updateUser(@PathVariable UUID userId, UserPostRequest user) {
        log.info("Received Request to update the user:{} with data:{}", userId, user);
        return userService.updateUser(userId, user);
    }

    @PostMapping("/{appType}")
    @Operation(summary = "Get Users by searching, filtering, appType, sorting, pagination etc...")
    public ResponseDto<UserPostResponse> getUsers(@PathVariable String appType, @RequestBody UserFilterRequest userFilterRequest, @RequestParam(value = "search", required = false) String search, @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received request for getting the users {}", search);
        return userService.getAllUsers(userFilterRequest, appType, search, pageable);
    }

    @GetMapping("/profile")
    @Operation(summary = "Get User Profile")
    public ResponseDto<ProfileResponse> getProfile(@RequestParam(value = "userName") String userName) {
        log.info("Received user get request for userName {}", userName);
        return userService.getProfile(userName);
    }

    @PostMapping("/{userId}/contacts")
    @Operation(summary = "Save/Update User Contact")
    public ResponseDto<String> saveUserContact(@PathVariable UUID userId, @RequestBody UserContactsRequest userContactsRequest) {
        log.info("Received user save/remove contact request for userId {}", userId);
        return userService.saveUserContacts(userId, userContactsRequest);
    }

    @GetMapping("/{userId}/contacts")
    @Operation(summary = "Get User Contacts")
    public ResponseDto<UserContactsResponse> getUserContacts(@PathVariable UUID userId, Pageable pageable) {
        log.info("Received user contact request for userId {}", userId);
        return userService.getUserContacts(userId, pageable);
    }

}
