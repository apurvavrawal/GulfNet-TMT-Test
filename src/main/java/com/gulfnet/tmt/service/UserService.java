package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.entity.sql.UserGroup;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.request.UserContactsRequest;
import com.gulfnet.tmt.model.request.UserFilterRequest;
import com.gulfnet.tmt.model.request.UserPostRequest;
import com.gulfnet.tmt.model.response.*;
import com.gulfnet.tmt.repository.sql.SettingsRepository;
import com.gulfnet.tmt.repository.sql.UserGroupRepository;
import com.gulfnet.tmt.service.chatservices.impl.ConversationServiceImpl;
import com.gulfnet.tmt.util.EmailTemplates;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.PasswordGenerator;
import com.gulfnet.tmt.util.enums.ConversationType;
import com.gulfnet.tmt.util.enums.Status;
import com.gulfnet.tmt.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static com.gulfnet.tmt.util.AppConstants.HQ_GROUP_CODE;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final GroupDao groupDao;
    private final UserGroupRepository userGroupRepository;
    private final ConversationServiceImpl conversationServiceImpl;
    private final UserValidator userValidator;
    private final ObjectMapper mapper;
    private final FileStorageService fileStorageService;
    private final SettingsRepository settingsRepository;
    private final EmailService emailService;
    private final GulfNetTMTServiceConfig gulfNetTMTServiceConfig;
    public ResponseDto<UserPostResponse> saveUser(UserPostRequest userPostRequest) {
        try {
            userValidator.validateUserRequest(userPostRequest, "CREATE");
            String password = PasswordGenerator.generatePatternedPassword(RandomGenerator.getDefault().nextInt(10, 15));
            User user = mapper.convertValue(userPostRequest, User.class);
            user.setPassword(EncryptionUtil.encrypt(password , gulfNetTMTServiceConfig.getAppSecurityKey()));
            user.setProfilePhoto(fileStorageService.uploadFile(userPostRequest.getProfilePhoto(), "User"));
            user = userDao.saveUser(user, userPostRequest.getUserRole(), userPostRequest.getUserGroup());

            // Adding new conversation entry for assigned groups for chat
            ConversationRequest conversationRequest = new ConversationRequest();

            List<UserGroup> userGroups = getAllGroups(user, userPostRequest.getUserGroup());
            userGroups.forEach(group ->{
                conversationRequest.setSenderId("");
                conversationRequest.setConsumerId(String.valueOf(group.getGroup().getId()));
                conversationRequest.setConversationType(ConversationType.GROUP);
                conversationServiceImpl.createConversation(conversationRequest);
            });

            //emailService.sendEmail(user.getEmail(),
            //        EmailTemplates.USER_ONBOARDING_SUBJECT,
            //        MessageFormat.format(EmailTemplates.USER_ONBOARDING_SUCCESS, userPostRequest.getFirstName(), userPostRequest.getLastName(), userPostRequest.getUserName(), password));
            return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(mapper.convertValue(user, UserPostResponse.class))).build();
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }

    public ResponseDto<UserPostResponse> getUser(UUID userId) {
        User user = userDao.findUser(userId).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        return ResponseDto.<UserPostResponse>builder().status(0).data(List.of(mapper.convertValue(user, UserPostResponse.class))).build();
    }

    public ResponseDto<UserPostResponse> updateUserProfile(UUID userId, String languagePreference) {
        userValidator.validateUserProfileRequest(languagePreference);
        User user = userDao.findUser(userId).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "User")));
        if (StringUtils.isNotEmpty(languagePreference)) {
            user.setLanguagePreference(languagePreference);
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

    public Optional<User> getUserByUserNameAndStatus(String userName) {
        return userDao.getUserByUserNameAndStatus(userName, Status.ACTIVE.getValue());
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

    public ResponseDto<UserPostResponse> getAllUsers(UserFilterRequest userFilterRequest, String appType, String search, Pageable pageable) {
        List<UserPostResponse> allUsers = new ArrayList<>();
        Page<User> users = userDao.findAll(appType, search, userFilterRequest, pageable);
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

    public ResponseDto<UserContactsResponse> getUserContacts(UUID userId, Pageable pageable) {
        UserContactsResponse userContactsResponse = userDao.getUserContacts(userId, pageable);
        return ResponseDto.<UserContactsResponse>builder()
                .data(List.of(userContactsResponse))
                .build();
    }

    public ResponseDto<String> saveUserContacts(UUID userId, UserContactsRequest userContactsRequest) {
        userDao.saveUserContacts(userId, userContactsRequest.getNewContactIds());
        userDao.removeUserContacts(userId, userContactsRequest.getRemoveContactIds());

        return ResponseDto.<String>builder()
                .data(List.of("Contact List Updated Successfully."))
                .build();
    }
    private List<UserGroup> getAllGroups(User user, List<String> groups) {
        if(!groups.contains(HQ_GROUP_CODE)) { groups.add(HQ_GROUP_CODE); }
        List<UserGroup> userGroups = new ArrayList<>();
        for(String groupCode : groups) {
            Group group = groupDao.findByCode(groupCode).orElseThrow(() -> new ValidationException(ErrorConstants.NOT_VALID_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_VALID_ERROR_MESSAGE, "Group : "+groupCode)));
            UserGroup userGroup = userGroupRepository.findByUserIdAndGroupId(user.getId(), group.getId());
            if (ObjectUtils.isNotEmpty(userGroup)) {
                userGroups.add(userGroup);
            } else {
                userGroup = new UserGroup();
                userGroup.setUser(user);
                userGroup.setGroup(group);
                userGroups.add(userGroup);
            }
        }
        return userGroups;
    }

}
