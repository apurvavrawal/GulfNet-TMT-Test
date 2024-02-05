package com.gulfnet.tmt.validator;


import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.ErrorDto;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileUploadValidator {

    private final GulfNetTMTServiceConfig gulfNetTMTServiceConfig;
    public void validate(MultipartFile file) {
        List<ErrorDto> errors = new ArrayList<>();

        if(!gulfNetTMTServiceConfig.getAllowedFileExt().contains(getFileExtension(file))) {
            errors.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE_DESC, "fileType" , "Valid FileTypes are "+ Arrays.toString(gulfNetTMTServiceConfig.getAllowedFileExt().toArray()))));
        }
        if(file.getSize() > gulfNetTMTServiceConfig.getMaxFileUploadSize()){
            errors.add(new ErrorDto(ErrorConstants.NOT_VALID_ERROR_CODE,
                    MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE_DESC, "fileSize", "Maximum Allowed size is "+gulfNetTMTServiceConfig.getMaxFileUploadSize())));
        }

        if (!errors.isEmpty()) throw new ValidationException(errors);

    }

    public String getFileExtension(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        return originalFilename != null ? FilenameUtils.getExtension(originalFilename) : null;
    }
}
