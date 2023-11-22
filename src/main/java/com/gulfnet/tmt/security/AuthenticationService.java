package com.gulfnet.tmt.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.LoginAudit;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.model.request.LoginRequest;
import com.gulfnet.tmt.repository.sql.LoginAuditRepository;
import com.gulfnet.tmt.service.EmailService;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.validator.LoginValidator;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserDao userDao;
    private final LoginAuditRepository loginAuditRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper mapper;
    private final EmailService emailService;
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
    public String signIn(String requestBody) {
        Optional<LoginRequest> loginRequest = getLoginRequest(requestBody);
        log.debug("loginRequest {}", loginRequest);
        LoginValidator.requestValidation(loginRequest);
        LoginRequest loginRequestData = loginRequest.get();
        User authenticatedUser = userDao.getAuthenticatedUser(loginRequestData.getUserName(), loginRequestData.getPassword());
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
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticatedUser.getUsername(), authenticatedUser.getPassword(), authenticatedUser.getAuthorities()));
        if (ObjectUtils.isEmpty(authenticatedUser)) {
            throw new GulfNetTMTException(ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_CODE, ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_MESSAGE);
        }
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

    private Optional<LoginRequest> getLoginRequest(String requestBody) {
        try {
            String decrypt = EncryptionUtil.decrypt(requestBody);
            return Optional.of(mapper.readValue(decrypt, LoginRequest.class));
        } catch (Exception e) {
            log.error("Error in LoginRequest decryption : ", e);
        }
        return Optional.empty();
    }
}
