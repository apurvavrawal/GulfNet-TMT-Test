package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.UserPostResponse;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.PasswordGenerator;
import com.gulfnet.tmt.util.enums.Status;
import com.gulfnet.tmt.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final UserValidator userValidator;
    private final ObjectMapper mapper;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;

    public ResponseDto<UserPostResponse> saveUser(UserPostRequest userPostRequest) {
        try {
            userValidator.validateCreateUserRequest(userPostRequest);
            String password = PasswordGenerator.generatePatternedPassword(RandomGenerator.getDefault().nextInt(10, 15));
            User user = mapper.convertValue(userPostRequest, User.class);
            user.setPassword(EncryptionUtil.encrypt(password));
            user.setStatus(Status.ACTIVE.getValue());
            user.setProfilePhoto(fileStorageService.uploadFile(userPostRequest.getProfilePhoto(),"User"));
            user = userDao.saveUser(user, userPostRequest.getUserRole());
            //TODO : Need to send Email to user
            return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(modelMapper.map(user, UserPostResponse.class))).build();
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }

    public ResponseDto<UserPostResponse> getUser(UUID userId) {
        User user = userDao.findUser(userId).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(modelMapper.map(user, UserPostResponse.class))).build();
    }

    public ResponseDto<UserPostResponse> updateUserProfile(UUID userId, MultipartFile profilePhoto, String languagePreference) {
        userValidator.validateUserProfileRequest(profilePhoto, languagePreference);
        User user = userDao.findUser(userId).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        if(StringUtils.isNotEmpty(languagePreference)){
            user.setLanguagePreference(languagePreference);
        }
        try {
            if (profilePhoto !=null && !profilePhoto.isEmpty()) {
                user.setProfilePhoto(fileStorageService.uploadFile(profilePhoto, "User"));
            }
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
        user = userDao.saveUser(user);
        return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(modelMapper.map(user, UserPostResponse.class))).build();
    }

    public User saveUser(User user) {
        return userDao.saveUser(user);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

}
