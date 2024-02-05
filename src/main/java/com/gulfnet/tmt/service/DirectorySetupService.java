package com.gulfnet.tmt.service;

import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class DirectorySetupService {

    @Value("${file.storage.base-directory}")
    private String baseDirectory;

    private final GroupService groupService;

    @PostConstruct
    public void init() {
        createDirectoryIfNotExists(baseDirectory + "/avatar/user");
        createDirectoryIfNotExists(baseDirectory + "/avatar/group");
        createDirectoryIfNotExists(baseDirectory + "/attachment/images");
        createDirectoryIfNotExists(baseDirectory + "/attachment/videos");
        createDirectoryIfNotExists(baseDirectory + "/attachment/audio");
        createDirectoryIfNotExists(baseDirectory + "/attachment/documents");
        createDirectoryIfNotExists(baseDirectory + "/attachment/common");
        ResponseDto<GroupResponse> groupsResponse = groupService.getAllGroups(null, Pageable.unpaged());
    }

    private void createDirectoryIfNotExists(String directoryPath) {
        Path path = Paths.get(directoryPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create directory: " + directoryPath, e);
            }
        }
    }
}
