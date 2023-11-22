package com.gulfnet.tmt.validator;

import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupValidator {

    private final GroupDao groupDao;

    public void validation(GroupRequest groupRequest) {
        List<String[]> errorMessages = new ArrayList<>();

        if (StringUtils.isEmpty(groupRequest.getCode())) {
            errorMessages.add(new String[]{ErrorConstants.MANDATORY_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "code")});
        } else if (groupDao.findByCode(groupRequest.getCode()).isPresent()) {
            errorMessages.add(new String[]{ErrorConstants.ALREADY_PRESENT_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.ALREADY_PRESENT_ERROR_MESSAGE, "Group Code", groupRequest.getCode())});
        }

        if (StringUtils.isEmpty(groupRequest.getName())) {
            errorMessages.add(new String[]{ErrorConstants.MANDATORY_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "name")});
        } else if (groupDao.findByName(groupRequest.getName()).isPresent()) {
            errorMessages.add(new String[]{ErrorConstants.ALREADY_PRESENT_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.ALREADY_PRESENT_ERROR_MESSAGE, "Group Name", groupRequest.getName())});
        }

        if (CollectionUtils.isNotEmpty(errorMessages)) {
            throw new ValidationException(errorMessages);
        }

    }
}
