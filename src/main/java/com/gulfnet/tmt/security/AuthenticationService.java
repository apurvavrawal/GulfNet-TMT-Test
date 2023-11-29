package com.gulfnet.tmt.security;

import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.LoginAudit;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.model.request.LoginRequest;
import com.gulfnet.tmt.repository.sql.LoginAuditRepository;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDao userDao;
    private final LoginAuditRepository loginAuditRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private int adminExpiryHr;
    private int mobileExpiryHr;
    @Value("${admin.token.expiry.time.hr}")
    public void setAdminExpiryHr(int adminExpiryHr) {
        this.adminExpiryHr = adminExpiryHr;
    }

    @Value("${mobile.token.expiry.time.hr}")
    public void setMobileExpiryHr(int mobileExpiryHr) {
        this.mobileExpiryHr = mobileExpiryHr;
    }
    public String signIn(LoginRequest loginRequestData) {
        User authenticatedUser = userDao.getAuthenticatedUser(loginRequestData.getUserName(), loginRequestData.getPassword(), loginRequestData.getAppType());
        Authentication authentication = authentication(authenticatedUser);
        Date expiryTime = getExpiryTime(loginRequestData);
        String jwtToken = createJWTToken(authentication, authenticatedUser, expiryTime);
        log.debug("jwtToken {}", jwtToken);
        saveLoginAudit(authenticatedUser, loginRequestData, expiryTime);
        return jwtToken;
    }

    private Date getExpiryTime(LoginRequest loginRequestData) {
        if("MOBILE".equalsIgnoreCase(loginRequestData.getAppType())){
            return DateUtils.addHours(new Date(), mobileExpiryHr);
        }
        return DateUtils.addHours(new Date(), adminExpiryHr);
    }

    private void saveLoginAudit(User authenticatedUser, LoginRequest loginRequestData, Date expiryTime) {
        LoginAudit loginAudit = new LoginAudit();
        loginAudit.setUserId(authenticatedUser.getId());
        loginAudit.setLocation(loginRequestData.getLocation());
        loginAudit.setMachineInfo(loginRequestData.getMachineInfo());
        loginAudit.setLoginExpiryDate(expiryTime);
        log.debug("loginAudit {}", loginAudit);
        loginAuditRepository.save(loginAudit);
    }

    private Authentication authentication(User authenticatedUser) {
        if (ObjectUtils.isEmpty(authenticatedUser)) {
            throw new GulfNetTMTException(ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_CODE, ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_MESSAGE);
        }
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticatedUser.getUsername(), authenticatedUser.getPassword(), authenticatedUser.getAuthorities()));
        log.debug("authenticate {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private String createJWTToken(Authentication authentication, User authenticatedUser, Date expiryTime) {
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        return jwtService.generateToken(authenticatedUser, expiryTime, scope);
    }
}
