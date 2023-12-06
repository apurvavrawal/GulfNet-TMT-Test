package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.LoginPostRequest;
import com.gulfnet.tmt.model.request.PasswordPostRequest;
import com.gulfnet.tmt.model.response.LoginResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.LoginService;
import com.gulfnet.tmt.util.enums.Action;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/{appType}/login")
    @Operation(summary = "Admin Login API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Encrypted of userName, password, appType, machineInfo, location values ")
    public ResponseDto<LoginResponse> adminLogin(@RequestBody LoginPostRequest loginRequest, @PathVariable String appType) {
        log.info("Received request for login {}", loginRequest);
        return loginService.login(loginRequest.getLoginRequest(), appType);
    }

    @GetMapping("/resetPassword")
    @Operation(summary = "ResetPassword Initiation API")
    public ResponseDto<String> resetPasswordRequest(@RequestParam String userName) {
        log.info("Received request for reset Password {}", userName);
        return loginService.resetPasswordRequest(userName, Action.RESET_PASSWORD);
    }

    @GetMapping("/verifyOTP")
    @Operation(summary = "Verify OTP API for resetPassword")
    public ResponseDto<String> verifyOTP(@RequestParam String userName, @RequestParam long otp) {
        log.info("Received request for reset Password {}", userName);
        return loginService.verifyOTP(userName, otp);
    }

    @PostMapping("/{appType}/resetPassword")
    @Operation(summary = "ResetPassword API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Encrypted of userName, otp, changePassword, confirmPassword")
    public ResponseDto<String> resetPassword(@RequestBody PasswordPostRequest passwordRequest, @PathVariable String appType) {
        log.info("Start processing reset password request {}", passwordRequest);
        return loginService.resetPassword(passwordRequest.getPasswordRequest(), appType);
    }

    @PostMapping("/{appType}/changePassword")
    @Operation(summary = "ChangePassword API")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Encrypted of userName, currentPassword, changePassword, confirmPassword")
    public ResponseDto<String> changePassword(@RequestBody PasswordPostRequest passwordRequest, @PathVariable String appType) {
        log.info("Received request for change Password {}", passwordRequest);
        return loginService.changePassword(passwordRequest.getPasswordRequest(), Action.CHANGE_PASSWORD, appType);
    }

}
