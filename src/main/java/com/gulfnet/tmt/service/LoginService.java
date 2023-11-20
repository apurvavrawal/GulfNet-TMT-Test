package com.gulfnet.tmt.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.entity.sql.UserPasswordAudit;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.PasswordRequest;
import com.gulfnet.tmt.model.response.LoginResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.sql.UserPasswordAuditRepository;
import com.gulfnet.tmt.security.AuthenticationService;
import com.gulfnet.tmt.util.EmailTemplates;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.Action;
import com.gulfnet.tmt.util.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.random.RandomGenerator;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final UserPasswordAuditRepository userPasswordAuditRepository;
    private final ObjectMapper mapper;
    private final EmailService emailService;

    public ResponseDto<LoginResponse> login(String requestBody) {
        LoginResponse loginResponse = new LoginResponse(authenticationService.signIn(requestBody));
        return ResponseDto.<LoginResponse>builder().status(0).data(List.of(loginResponse)).build();
    }

    public ResponseDto<String> changePassword(String requestBody, Action action) {
        PasswordRequest passwordRequest = getPasswordRequest(requestBody)
                .orElseThrow(() -> new GulfNetTMTException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE));
        validatedPasswordRequest(passwordRequest);
        User user = userService.getUserByUserName(passwordRequest.getUserName()).orElseThrow(() -> new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE));
        if (user.getPassword().equals(EncryptionUtil.encrypt(passwordRequest.getCurrentPassword()))) {
            user.setPassword(EncryptionUtil.encrypt(passwordRequest.getConfirmPassword()));
            userService.saveUser(user);
            saveUserPasswordAudit(action, user);
            emailService.sendEmail(user.getEmail(),
                    EmailTemplates.PASSWORD_CHANGE_SUBJECT,
                    MessageFormat.format(EmailTemplates.CHANGE_PASSWORD_SUCCESS, passwordRequest.getUserName()));
            return ResponseDto.<String>builder().status(0).data(List.of("Password updated Successfully.")).build();
        }
        throw new ValidationException(ErrorConstants.NOT_MATCH_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_MATCH_ERROR_MESSAGE, List.of("DB Current Password", "Request Current Password")));
    }

    public ResponseDto<String> resetPasswordRequest(String userName, Action action) {
        User user = userService.getUserByUserName(userName).orElseThrow(() -> new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE));
        updateExistingRequest(user.getId());
        UserPasswordAudit userPasswordAudit = buildUserPasswordAudit(action, user);
        emailService.sendEmail(user.getEmail(),
                EmailTemplates.RESET_PASSWORD_SUBJECT,
                MessageFormat.format(EmailTemplates.RESET_PASSWORD_REQUEST, userName, String.valueOf(userPasswordAudit.getOtp())));
        userPasswordAuditRepository.save(userPasswordAudit);
        return ResponseDto.<String>builder().status(0).data(List.of("Password request send Successfully.")).build();
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

    public ResponseDto<String> resetPassword(String requestBody) {
        PasswordRequest passwordRequest = getPasswordRequest(requestBody)
                .orElseThrow(() -> new GulfNetTMTException(ErrorConstants.DECRYPTION_ERROR_CODE, ErrorConstants.DECRYPTION_ERROR_MESSAGE));
        User user = userService.getUserByUserName(passwordRequest.getUserName())
                .orElseThrow(() -> new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, ErrorConstants.NOT_FOUND_ERROR_MESSAGE));
        validatedPasswordRequest(passwordRequest);
        List<UserPasswordAudit> userPasswordAudits = userPasswordAuditRepository.findByUserIdAndStatusAndAction(user.getId(), Status.PENDING.getValue(), Action.RESET_PASSWORD.getValue());
        if (CollectionUtils.isNotEmpty(userPasswordAudits)) {
            updateResetPassword(userPasswordAudits.get(0), user, passwordRequest);
            return ResponseDto.<String>builder().status(0).data(List.of("Password update Successfully.")).build();
        }
        throw new GulfNetTMTException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_CODE, "User"));
    }
    private void validatedPasswordRequest(PasswordRequest passwordRequest) {
        if (StringUtils.isEmpty(passwordRequest.getChangePassword())) {
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_CODE, "ChangePassword"));
        }
        if (StringUtils.isEmpty(passwordRequest.getConfirmPassword())) {
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_CODE, "ConfirmPassword"));
        }
        if (!passwordRequest.getChangePassword().equals(passwordRequest.getConfirmPassword())) {
            throw new ValidationException(ErrorConstants.NOT_MATCH_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_MATCH_ERROR_MESSAGE, List.of("ChangePassword", "ConfirmPassword")));
        }
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
        user.setPassword(EncryptionUtil.encrypt(passwordRequest.getConfirmPassword()));
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
    private Optional<PasswordRequest> getPasswordRequest(String requestBody) {
        try {
            String decrypt = EncryptionUtil.decrypt(requestBody);
            return Optional.of(mapper.readValue(decrypt, PasswordRequest.class));
        } catch (JsonProcessingException e) {
           throw new GulfNetTMTException(ErrorConstants.NOT_VALID_ERROR_CODE, "Error in mapping");
        }
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
        userPasswordAudit.setOtp(RandomGenerator.getDefault().nextLong(999999));
        userPasswordAudit.setOtpExpiryDate(DateUtils.addMinutes(new Date(), 10));
        return userPasswordAudit;
    }
}


