package com.gulfnet.tmt.validator;

import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.PasswordRequest;
import com.gulfnet.tmt.util.AppConstants;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.Action;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.regex.Pattern;

public final class PasswordValidator {

    public static void requestValidation(PasswordRequest passwordRequest, User user, Action action, String key) {
        if (StringUtils.isEmpty(passwordRequest.getChangePassword())) {
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "ChangePassword"));
        }
        if (StringUtils.isEmpty(passwordRequest.getConfirmPassword())) {
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "ConfirmPassword"));
        }
        if (!passwordRequest.getChangePassword().equals(passwordRequest.getConfirmPassword())) {
            throw new ValidationException(ErrorConstants.NOT_MATCH_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_MATCH_ERROR_MESSAGE, "ChangePassword", "ConfirmPassword"));
        }
        if (!Pattern.matches("^(?=.*[A-Z])(?=.*[\\W])(?=.*[0-9])(?=.*[a-z]).{8,15}$", passwordRequest.getChangePassword())) {
            throw new ValidationException(ErrorConstants.NOT_VALID_ERROR_CODE, ErrorConstants.NOT_VALID_ERROR_MESSAGE_PASSWORD);
        }
        if ((Action.CHANGE_PASSWORD == action) && (!user.getPassword().equals(EncryptionUtil.encrypt(passwordRequest.getCurrentPassword(), key)))) {
            throw new ValidationException(ErrorConstants.NOT_MATCH_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_MATCH_ERROR_MESSAGE, "System Current Password", "Request Current Password"));
        }
        String password = EncryptionUtil.adminDecrypt(user.getPassword(), key);
        if (password.equals(passwordRequest.getChangePassword())) {
            throw new GulfNetTMTException(ErrorConstants.NOT_VALID_ERROR_CODE, ErrorConstants.NOT_VALID_ERROR_MESSAGE_CHANGE_PASSWORD);
        }
    }
}
