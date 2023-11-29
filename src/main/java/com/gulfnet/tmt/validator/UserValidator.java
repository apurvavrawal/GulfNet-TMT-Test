package com.gulfnet.tmt.validator;

import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.dao.AppRoleDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.AppRole;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.ErrorDto;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.Language;
import liquibase.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public final class UserValidator {

    private final UserDao userDao;

    private final AppRoleDao appRoleDao;

    private final FileUploadValidator fileUploadValidator;

    private final GulfNetTMTServiceConfig gulfNetTMTServiceConfig;
    private final List<String> APP_TYPE_MOBILE = List.of("ADMIN", "MOBILE");

    public void validateCreateUserRequest(UserPostRequest user) {
        List<ErrorDto> errors = userBasicValidation(user);
        if(!user.getProfilePhoto().isEmpty()) {
            try {
                fileUploadValidator.validate(user.getProfilePhoto());
            } catch (ValidationException ex) {
                errors.addAll(ex.getErrorMessages());
            }
        }
        if (!errors.isEmpty()) throw new ValidationException(errors);
    }

    private List<ErrorDto> userBasicValidation(UserPostRequest user) {
        List<ErrorDto> errors = new ArrayList<>();

        if (StringUtils.isEmpty(user.getUserName())) {
            errors.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "User Name")));
        } else if (isUsernameExists(user.getUserName())) {
            errors.add(new ErrorDto(ErrorConstants.ALREADY_PRESENT_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.ALREADY_PRESENT_ERROR_MESSAGE, "UserName")));
        }

        if (StringUtils.isEmpty(user.getFirstName())){
            errors.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "First Name")));
        }

        if (StringUtils.isEmpty(user.getEmail())){
            errors.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "Email")));
        } else if (!Pattern.matches(gulfNetTMTServiceConfig.getRegExEmail(), user.getEmail())) {
            errors.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "Email")));
        }

        if (StringUtils.isEmpty(user.getPhone())){
            errors.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "Phone")));
        } else if (!Pattern.matches(gulfNetTMTServiceConfig.getRegExPhone(), user.getPhone())) {
            errors.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "Phone")));
        }

        if (!APP_TYPE_MOBILE.contains(user.getAppType())) {
            errors.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "AppType")));
        }

        if (StringUtil.isEmpty(user.getLanguagePreference())){
            user.setLanguagePreference(gulfNetTMTServiceConfig.getDefaultUserLanguage());
        }
        if (Language.get(user.getLanguagePreference()).isEmpty()) {
            errors.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, user.getLanguagePreference() + " language ")));
        }

        if (CollectionUtils.isEmpty(user.getUserRole())) {
            errors.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "User Role")));
        } else if (!isValidRoles(user.getUserRole())) {
            errors.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "User Role")));
        }
        return errors;
    }

    private boolean isValidRoles(List<String> userRoles) {
        return appRoleDao.getAppRolesByCode(userRoles.stream()
              .map(String::toUpperCase)
              .collect(Collectors.toList())).size() == userRoles.size();
    }
    private boolean isUsernameExists(String username) {
        return userDao.getUserByUserName(username).isPresent();
    }
}
