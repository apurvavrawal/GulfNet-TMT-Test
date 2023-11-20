package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.LoginPostRequest;
import com.gulfnet.tmt.model.request.PasswordPostRequest;
import com.gulfnet.tmt.model.response.LoginResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.LoginService;
import com.gulfnet.tmt.util.enums.Action;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseDto<LoginResponse> loginUser(@RequestBody LoginPostRequest loginRequest) {
        log.info("Received request for login {}", loginRequest);
        return loginService.login(loginRequest.getLoginRequest());
    }

    @GetMapping("/resetPassword")
    public ResponseDto<String> resetPasswordRequest(@RequestParam String userName) {
        log.info("Received request for reset Password {}", userName);
        return loginService.resetPasswordRequest(userName, Action.RESET_PASSWORD);
    }

    @GetMapping("/verifyOTP")
    public ResponseDto<String> verifyOTP(@RequestParam String userName, @RequestParam long otp) {
        log.info("Received request for reset Password {}", userName);
        return loginService.verifyOTP(userName, otp);
    }

    @PostMapping("/resetPassword")
    public ResponseDto<String> resetPassword(@RequestBody PasswordPostRequest passwordRequest) {
        log.info("Start processing reset password request {}", passwordRequest);
        return loginService.resetPassword(passwordRequest.getPasswordRequest());
    }

    @PostMapping("/changePassword")
    public ResponseDto<String> changePassword(@RequestBody PasswordPostRequest passwordRequest) {
        log.info("Received request for change Password {}", passwordRequest);
        return loginService.changePassword(passwordRequest.getPasswordRequest(), Action.CHANGE_PASSWORD);
    }

}
