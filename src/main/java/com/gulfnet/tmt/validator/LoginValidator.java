package com.gulfnet.tmt.validator;

import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.LoginRequest;
import com.gulfnet.tmt.util.ErrorConstants;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.Optional;

public final class LoginValidator {

    public static void requestValidation(Optional<LoginRequest> loginRequest){
        if (loginRequest.isEmpty()) {
            throw new GulfNetTMTException(ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_CODE, ErrorConstants.LOGIN_USER_NOT_FOUND_ERROR_MESSAGE);
        }
        LoginRequest request = loginRequest.get();
        if(StringUtils.isEmpty(request.getUserName())){
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "userName"));
        }
        if(StringUtils.isEmpty(request.getPassword())){
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "password"));
        }
        if(StringUtils.isEmpty(request.getAppType())){
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "appType"));
        }
        if(StringUtils.isEmpty(request.getLocation())){
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "location"));
        }
        if(StringUtils.isEmpty(request.getMachineInfo())){
            throw new ValidationException(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "machineInfo"));
        }
        if(!"ADMIN".equalsIgnoreCase(request.getAppType()) && !"MOBILE".equalsIgnoreCase(request.getAppType())){
            throw new ValidationException(ErrorConstants.NOT_VALID_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "appType"));
        }
    }
}
