package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.ProfileResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.SettingsResponse;
import com.gulfnet.tmt.model.response.UserPostResponse;
import com.gulfnet.tmt.repository.sql.SettingsRepository;
import com.gulfnet.tmt.util.EmailTemplates;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.PasswordGenerator;
import com.gulfnet.tmt.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
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
    private final SettingsRepository settingsRepository;
    private final EmailService emailService;

    public ResponseDto<UserPostResponse> saveUser(UserPostRequest userPostRequest) {
        try {
            userValidator.validateUserRequest(userPostRequest, "CREATE");
            String password = PasswordGenerator.generatePatternedPassword(RandomGenerator.getDefault().nextInt(10, 15));
            User user = mapper.convertValue(userPostRequest, User.class);
            user.setPassword(EncryptionUtil.encrypt(password));
            user.setProfilePhoto(fileStorageService.uploadFile(userPostRequest.getProfilePhoto(), "User"));
            user = userDao.saveUser(user, userPostRequest.getUserRole(), userPostRequest.getUserGroup());
            //TODO : Need to send Email to user
            emailService.sendEmail(user.getEmail(),
                    EmailTemplates.USER_ONBOARDING_SUCCESS,
                    MessageFormat.format(EmailTemplates.USER_ONBOARDING_SUCCESS, userPostRequest.getFirstName(), userPostRequest.getLastName(), userPostRequest.getUserName(), password));
            return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(mapper.convertValue(user, UserPostResponse.class))).build();
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }

    public ResponseDto<UserPostResponse> getUser(UUID userId) {
        User user = userDao.findUser(userId).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(mapper.convertValue(user, UserPostResponse.class))).build();
    }

    public ResponseDto<UserPostResponse> updateUserProfile(UUID userId, MultipartFile profilePhoto, String languagePreference) {
        userValidator.validateUserProfileRequest(profilePhoto, languagePreference);
        User user = userDao.findUser(userId).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        if (StringUtils.isNotEmpty(languagePreference)) {
            user.setLanguagePreference(languagePreference);
        }
        try {
            if (profilePhoto != null && !profilePhoto.isEmpty()) {
                user.setProfilePhoto(fileStorageService.uploadFile(profilePhoto, "User"));
            }
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
        user = saveUser(user);
        return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(mapper.convertValue(user, UserPostResponse.class))).build();
    }

    public User saveUser(User user) {
        return userDao.saveUser(user);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }

    public ResponseDto<UserPostResponse> updateUser(UUID userId, UserPostRequest userPostRequest) {
        userValidator.validateUserRequest(userPostRequest, "UPDATE");
        User userDB = userDao.findUser(userId).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        User user = mapper.convertValue(userPostRequest, User.class);
        log.info("Starting DB Update for userID:{}", userId);
        user.setId(userDB.getId());
        user.setPassword(userDB.getPassword());
        user.setCreatedBy(userDB.getCreatedBy());
        user.setDateCreated(userDB.getDateCreated());
        try {
            if (userPostRequest.getProfilePhoto() != null && !userPostRequest.getProfilePhoto().isEmpty()) {
                user.setProfilePhoto(fileStorageService.uploadFile(userPostRequest.getProfilePhoto(), "User"));
            } else {
                user.setProfilePhoto(userDB.getProfilePhoto());
            }
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
        User userUpdated = userDao.saveUser(user, userPostRequest.getUserRole(), userPostRequest.getUserGroup());
        log.info("DB Update successful for userID:{}", userId);
        return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(mapper.convertValue(userUpdated, UserPostResponse.class))).build();
    }

    public ResponseDto<UserPostResponse> getAllUsers(String search, Pageable pageable) {
        List<UserPostResponse> allUsers = new ArrayList<>();
        Page<User> users = userDao.findAll(search, pageable);
        log.info(" Users data from page number:{}, page size:{}", pageable.getPageNumber(), pageable.getPageSize());
        users.stream().forEach(u -> allUsers.add(mapper.convertValue(u, UserPostResponse.class)));
        return ResponseDto.<UserPostResponse>builder().status(0)
                .data(allUsers)
                .count((long) allUsers.size())
                .total(users.getTotalElements())
                .build();
    }

    public ResponseDto<ProfileResponse> getProfile(String userName) {
        User userDB = userDao.findByUserName(userName).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        SettingsResponse settingsResponse = mapper.convertValue(settingsRepository.findAll().get(0), SettingsResponse.class);
        ProfileResponse profileResponse = ProfileResponse.builder()
                .support(settingsResponse)
                .user(mapper.convertValue(userDB, UserPostResponse.class))
                .build();
        return ResponseDto.<ProfileResponse>builder()
                .data(List.of(profileResponse))
                .build();
    }

}
