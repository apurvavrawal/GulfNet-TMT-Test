package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.StampDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.entity.sql.Stamp;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.StampResponse;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.ResponseMessage;
import com.gulfnet.tmt.util.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StampService {

    private final ObjectMapper mapper;
    private final StampDao stampDao;
    private final FileStorageService fileStorageService;

    public ResponseDto<StampResponse> saveStamp(MultipartFile file) {
        try {
            Stamp stampBuilder = Stamp.builder()
                    .stampFile(fileStorageService.uploadFile(file, "Stamp"))
                    .status(Status.ACTIVE.getName())
                    .build();
            Stamp stamp = stampDao.saveStamp(stampBuilder);
            StampResponse stampResponse = mapper.convertValue(stamp, StampResponse.class);
            return ResponseDto.<StampResponse>builder()
                    .data(List.of(stampResponse))
                    .build();
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }

    public ResponseDto<StampResponse> getAllStamp(Pageable pageable) {
        Page<Stamp> stampInfoPage;
        stampInfoPage = stampDao.findAll(pageable);
        List<StampResponse> stampResponses = new ArrayList<>();
        for (Stamp stamp : stampInfoPage.getContent()) {
            stampResponses.add(mapper.convertValue(stamp, StampResponse.class));
        }
        Long countStamp = stampDao.getCount();
        return ResponseDto.<StampResponse>builder()
                .data(stampResponses)
                .count(stampInfoPage.stream().count())
                .total(countStamp)
                .build();
    }

    public ResponseDto<StampResponse> getStampById(UUID id) {
        Stamp stamp = stampDao.findByIdAndStatusI(id, Status.ACTIVE.getName());
        StampResponse stampResponse = mapper.convertValue(stamp, StampResponse.class);
        if (ObjectUtils.isEmpty(stampResponse)) {
            return ResponseDto.<StampResponse>builder()
                    .data(null)
                    .build();
        } else {
            return ResponseDto.<StampResponse>builder()
                    .data(List.of(stampResponse))
                    .build();
        }
    }

    public ResponseDto<StampResponse> deleteStampById(UUID id) {
        Stamp stamp = stampDao.findById(id).orElseThrow(
                () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Stamp")));
        if (ObjectUtils.isEmpty(stamp)) {
            return ResponseDto.<StampResponse>builder().data(null).build();
        } else {
            stampDao.updateStampStatusById(Status.INACTIVE.getName(), id);
            return ResponseDto.<StampResponse>builder()
                    .status(0)
                    .message("Stamp deleted successfully")
                    .build();
        }
    }

}