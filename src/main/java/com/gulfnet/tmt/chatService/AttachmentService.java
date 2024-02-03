package com.gulfnet.tmt.chatService;

import com.gulfnet.tmt.model.response.AttachmentResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    ResponseDto<List<AttachmentResponse>> uploadFiles(List<MultipartFile> files, String attachmentType);

    Resource loadFileAsResource(String decodedFilePath);
}
