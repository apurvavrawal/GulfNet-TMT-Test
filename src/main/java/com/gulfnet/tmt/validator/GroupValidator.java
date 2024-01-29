package com.gulfnet.tmt.validator;

import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.ErrorDto;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupValidator {

    private final GroupDao groupDao;

    public void validation(GroupRequest groupRequest, Optional<UUID> groupId) {
        List<ErrorDto> errorMessages = new ArrayList<>();

        if (StringUtils.isEmpty(groupRequest.getCode())) {
            errorMessages.add(ErrorDto.builder()
                    .errorCode(ErrorConstants.MANDATORY_ERROR_CODE)
                    .errorMessage(MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "Code"))
                    .build());
        } else {
            Optional<Group> group = groupDao.findByCode(groupRequest.getCode());
            if((groupId.isEmpty() && group.isPresent()) || (group.isPresent() && !group.get().getId().equals(groupId.get()))) {
                errorMessages.add(ErrorDto.builder()
                        .errorCode(ErrorConstants.ALREADY_PRESENT_ERROR_CODE)
                        .errorMessage(MessageFormat.format(ErrorConstants.ALREADY_PRESENT_ERROR_MESSAGE, "Code", groupRequest.getCode()))
                        .build());
            }
        }

        if (StringUtils.isEmpty(groupRequest.getName())) {
            errorMessages.add(ErrorDto.builder()
                    .errorCode(ErrorConstants.MANDATORY_ERROR_CODE)
                    .errorMessage(MessageFormat.format(ErrorConstants.MANDATORY_ERROR_MESSAGE, "Name"))
                    .build());
        } else {
            Optional<Group> group = groupDao.findByName(groupRequest.getName());
            if((groupId.isEmpty() && group.isPresent()) || (group.isPresent() && !group.get().getId().equals(groupId.get()))) {
                errorMessages.add(ErrorDto.builder()
                        .errorCode(ErrorConstants.ALREADY_PRESENT_ERROR_CODE)
                        .errorMessage(MessageFormat.format(ErrorConstants.ALREADY_PRESENT_ERROR_MESSAGE, "Name", groupRequest.getName()))
                        .build());
            }
        }

        if (CollectionUtils.isNotEmpty(errorMessages)) {
            throw new ValidationException(errorMessages);
        }

    }

}
