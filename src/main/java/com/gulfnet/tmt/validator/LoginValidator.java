package com.gulfnet.tmt.validator;

import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.LoginRequest;
import com.gulfnet.tmt.model.response.ErrorDto;
import com.gulfnet.tmt.util.ErrorConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class LoginValidator {

    public static void requestValidation(Optional<LoginRequest> loginRequest) {
        List<ErrorDto> errorMessages = new ArrayList<>();
        if (loginRequest.isEmpty()) {
            errorMessages.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE,"Login Request")));
        }
        LoginRequest request = loginRequest.get();
        if(StringUtils.isEmpty(request.getUserName())){
            errorMessages.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "userName")));
        }
        if(StringUtils.isEmpty(request.getPassword())){
            errorMessages.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "password")));
        }
        if(StringUtils.isEmpty(request.getAppType())){
            errorMessages.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "appType")));
        }
        if(StringUtils.isEmpty(request.getLocation())){
            errorMessages.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "location")));
        }
        if(StringUtils.isEmpty(request.getMachineInfo())){
            errorMessages.add(new ErrorDto(ErrorConstants.MANDATORY_ERROR_CODE, MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "machineInfo")));
        }
        if(!"ADMIN".equalsIgnoreCase(request.getAppType()) && !"MOBILE".equalsIgnoreCase(request.getAppType())){
            errorMessages.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "appType")));
        }
        if (CollectionUtils.isNotEmpty(errorMessages)) {
            throw new ValidationException(errorMessages);
        }
    }
}
