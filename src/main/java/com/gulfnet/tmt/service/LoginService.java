package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.entity.sql.UserPasswordAudit;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.LoginRequest;
import com.gulfnet.tmt.model.request.PasswordRequest;
import com.gulfnet.tmt.model.response.LoginResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.sql.UserPasswordAuditRepository;
import com.gulfnet.tmt.security.AuthenticationService;
import com.gulfnet.tmt.util.AppConstants;
import com.gulfnet.tmt.util.EmailTemplates;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.Action;
import com.gulfnet.tmt.util.enums.Status;
import com.gulfnet.tmt.validator.LoginValidator;
import com.gulfnet.tmt.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserPasswordAuditRepository userPasswordAuditRepository;
    private final ObjectMapper mapper;
    private final EmailService emailService;
    private final GulfNetTMTServiceConfig gulfNetTMTServiceConfig;

    public ResponseDto<LoginResponse> login(String requestBody, String appType) {
        Optional<LoginRequest> loginRequest = getLoginRequest(requestBody, appType);
        log.debug("loginRequest {}", loginRequest);
        if (loginRequest.isEmpty()) {
            throw new ValidationException(ErrorConstants.NOT_VALID_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE,"Login Request"));
        }
        LoginRequest loginRequestData = loginRequest.get();
        LoginValidator.requestValidation(loginRequestData,appType);
        LoginResponse loginResponse = new LoginResponse(authenticationService.signIn(loginRequestData));
        return ResponseDto.<LoginResponse>builder().status(0).data(List.of(loginResponse)).build();
    }

    public ResponseDto<String> changePassword(String requestBody, Action action, String appType) {
        PasswordRequest passwordRequest = getPasswordRequest(requestBody, appType)
                .orElseThrow(() -> new GulfNetTMTException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE));
        User user = userService.getUserByUserName(passwordRequest.getUserName()).orElseThrow(() -> new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE));
        PasswordValidator.requestValidation(passwordRequest, user, action, gulfNetTMTServiceConfig.getAppSecurityKey());
        user.setPassword(EncryptionUtil.encrypt(passwordRequest.getConfirmPassword(), gulfNetTMTServiceConfig.getAppSecurityKey()));
        userService.saveUser(user);
        saveUserPasswordAudit(action, user);
        emailService.sendEmail(user.getEmail(),
                EmailTemplates.PASSWORD_CHANGE_SUBJECT,
                MessageFormat.format(EmailTemplates.CHANGE_PASSWORD_SUCCESS, passwordRequest.getUserName()));
        return ResponseDto.<String>builder().status(0).data(List.of("Password updated Successfully.")).build();
    }

    public ResponseDto<String> resetPasswordRequest(String userName, Action action) {
        User user = userService.getUserByUserName(userName).orElseThrow(() -> new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE));
        updateExistingRequest(user.getId());
        UserPasswordAudit userPasswordAudit = buildUserPasswordAudit(action, user);
        emailService.sendEmail(user.getEmail(),
                EmailTemplates.RESET_PASSWORD_SUBJECT,
                MessageFormat.format(EmailTemplates.RESET_PASSWORD_REQUEST, userName, String.valueOf(userPasswordAudit.getOtp())));
        userPasswordAuditRepository.save(userPasswordAudit);
        return ResponseDto.<String>builder().status(0).data(List.of("OTP send Successfully for Reset Password .")).build();
    }

    public ResponseDto<String> verifyOTP(String userName, long otp) {
        User user = userService.getUserByUserName(userName).orElseThrow(() -> new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE));
        List<UserPasswordAudit> userPasswordAudits = userPasswordAuditRepository.findByUserIdAndStatusAndAction(user.getId(), Status.PENDING.getValue(), Action.RESET_PASSWORD.getValue());
        if (CollectionUtils.isNotEmpty(userPasswordAudits)) {
            UserPasswordAudit userPasswordAudit = userPasswordAudits.get(0);
            validateOTP(otp, userPasswordAudit);
            return ResponseDto.<String>builder().status(0).data(List.of("Otp Verified Successfully")).build();
        }
        throw new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_CODE, "User"));
    }

    public ResponseDto<String> resetPassword(String requestBody, Action action, String appType) {
        PasswordRequest passwordRequest = getPasswordRequest(requestBody, appType)
                .orElseThrow(() -> new GulfNetTMTException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE));
        User user = userService.getUserByUserName(passwordRequest.getUserName())
                .orElseThrow(() -> new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE));
        PasswordValidator.requestValidation(passwordRequest, user, action, gulfNetTMTServiceConfig.getAppSecurityKey());
        List<UserPasswordAudit> userPasswordAudits = userPasswordAuditRepository.findByUserIdAndStatusAndAction(user.getId(), Status.PENDING.getValue(), Action.RESET_PASSWORD.getValue());
        if (CollectionUtils.isNotEmpty(userPasswordAudits)) {
            updateResetPassword(userPasswordAudits.get(0), user, passwordRequest);
            return ResponseDto.<String>builder().status(0).data(List.of("Password update Successfully.")).build();
        }
        throw new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_CODE, "User"));
    }
    private static void validateOTP(long otp, UserPasswordAudit userPasswordAudit) {
        if (!Objects.equals(userPasswordAudit.getOtp(), otp)) {
            throw new ValidationException(ErrorConstants.NOT_VALID_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "OTP"));
        }
        if (userPasswordAudit.getOtpExpiryDate().before(new Date())) {
            throw new ValidationException(ErrorConstants.NOT_VALID_ERROR_CODE, ErrorConstants.OTP_EXPIRED);
        }
    }
    private void updateResetPassword(UserPasswordAudit userPasswordAudit, User user, PasswordRequest passwordRequest) {
        validateOTP(userPasswordAudit.getOtp(), userPasswordAudit);
        user.setPassword(EncryptionUtil.encrypt(passwordRequest.getConfirmPassword(), gulfNetTMTServiceConfig.getAppSecurityKey()));
        userService.saveUser(user);
        userPasswordAudit.setStatus(Status.COMPLETED.getValue());
        userPasswordAuditRepository.save(userPasswordAudit);
        emailService.sendEmail(user.getEmail(),
                EmailTemplates.PASSWORD_CHANGE_SUBJECT,
                MessageFormat.format(EmailTemplates.CHANGE_PASSWORD_SUCCESS, passwordRequest.getUserName()));
    }

    private void saveUserPasswordAudit(Action action, User user) {
        UserPasswordAudit userPasswordAudit = new UserPasswordAudit();
        userPasswordAudit.setAction(action.getValue());
        userPasswordAudit.setUserId(user.getId());
        userPasswordAudit.setStatus(Status.COMPLETED.getValue());
        userPasswordAuditRepository.save(userPasswordAudit);
    }
    private Optional<PasswordRequest> getPasswordRequest(String requestBody, String appType) {
        return getDecryptedRequest(requestBody, appType, PasswordRequest.class);
    }
    private void updateExistingRequest(UUID userId) {
        List<UserPasswordAudit> userPasswordAudits = userPasswordAuditRepository.findByUserIdAndStatusAndAction(userId, Status.PENDING.getValue(), Action.RESET_PASSWORD.getValue());
        if (CollectionUtils.isNotEmpty(userPasswordAudits)) {
            for (UserPasswordAudit existingUserPasswordAudit : userPasswordAudits) {
                existingUserPasswordAudit.setStatus(Status.INACTIVE.getValue());
                userPasswordAuditRepository.save(existingUserPasswordAudit);
            }
        }
    }
    private static UserPasswordAudit buildUserPasswordAudit(Action action, User user) {
        UserPasswordAudit userPasswordAudit = new UserPasswordAudit();
        userPasswordAudit.setAction(action.getValue());
        userPasswordAudit.setUserId(user.getId());
        userPasswordAudit.setStatus(Status.PENDING.getValue());
        userPasswordAudit.setOtp(RandomGenerator.getDefault().nextLong(111111, 999999));
        userPasswordAudit.setOtpExpiryDate(DateUtils.addMinutes(new Date(), 10));
        return userPasswordAudit;
    }

    private Optional<LoginRequest> getLoginRequest(String requestBody, String appType) {
        return getDecryptedRequest(requestBody, appType, LoginRequest.class);
    }

    private Optional getDecryptedRequest(String requestBody, String appType, Class clazz) {
        try {
            if (appType.equalsIgnoreCase(AppConstants.APP_TYPE_MOBILE.get(0))) {
                String decrypt = EncryptionUtil.adminDecrypt(requestBody, gulfNetTMTServiceConfig.getAppSecurityKey());
                return of(mapper.readValue(decrypt, clazz));
            } else if (appType.equalsIgnoreCase(AppConstants.APP_TYPE_MOBILE.get(1))) {
                String decrypt = EncryptionUtil.mobileDecrypt(requestBody, gulfNetTMTServiceConfig.getAppSecurityKey());
                return of(mapper.readValue(decrypt, clazz));
            }
        } catch (Exception e) {
            log.error("Error in getLoginRequest request for requestBody {} , appType {}", requestBody, appType, e);
        }

        return empty();
    }
}


