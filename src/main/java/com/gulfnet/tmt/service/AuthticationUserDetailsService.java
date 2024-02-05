package com.gulfnet.tmt.service;

import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.util.ErrorConstants;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthticationUserDetailsService {

    private final UserService userService;

    public AuthticationUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    public UserDetailsService userDetailsService() {
        return username -> userService.getUserByUserName(username)
                .orElseThrow(() -> new GulfNetTMTException(ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_CODE, ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_MESSAGE));
    }
}
