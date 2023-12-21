package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.*;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.UserFilterRequest;
import com.gulfnet.tmt.model.response.UserBasicInfoResponse;
import com.gulfnet.tmt.model.response.UserContactsResponse;
import com.gulfnet.tmt.repository.jdbc.UserContactJDBCRepository;
import com.gulfnet.tmt.repository.sql.LoginAuditRepository;
import com.gulfnet.tmt.repository.sql.UserContactRepository;
import com.gulfnet.tmt.repository.sql.UserGroupRepository;
import com.gulfnet.tmt.repository.sql.UserRepository;
import com.gulfnet.tmt.repository.sql.UserRoleRepository;
import com.gulfnet.tmt.specifications.UserSpecifications;
import com.gulfnet.tmt.util.EncryptionUtil;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.Status;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gulfnet.tmt.util.AppConstants.APP_TYPE_MOBILE;
import static com.gulfnet.tmt.util.AppConstants.HQ_GROUP_CODE;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserContactJDBCRepository userContactJDBCRepository;
    private final UserContactRepository userContactRepository;
    private final AppRoleDao appRoleDao;
    private final GroupDao groupDao;
    private final LoginAuditRepository loginAuditRepository;
    private static final String key = "4995f5e3-0280-4e6a-ad40-917136cbb884";
    public Optional<User> getAuthenticatedUser(String username, String password, String appType) {
        log.info("UserName : {}, password : {}", username, EncryptionUtil.encrypt(password, key));
        return userRepository.findByUserNameAndPasswordAndAppTypeAndStatus(username, EncryptionUtil.encrypt(password, key), appType, Status.ACTIVE.getValue());
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUser(UUID userId) {
        return userRepository.findById(userId);
    }
    @Transactional
    public User saveUser(User user, List<String> role, List<String> groups) {
        List<UserRole> userRoles = getUserRoles(user, role);
        user.setUserRole(userRoles);
        if (APP_TYPE_MOBILE.get(1).equalsIgnoreCase(user.getAppType())) {
            List<UserGroup> userGroups = getUserGroups(user, groups);
            user.setUserGroups(userGroups);
        } else {
            user.setUserGroups(new ArrayList<>());
        }
        user = userRepository.save(user);
        return user;
    }
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Optional<User> getUserByUserNameAndStatus(String userName, String status) {
        return userRepository.findByUserNameAndStatus(userName, status);
    }
    public Page<User> findAll(String appType, String search, Pageable pageable) {
        Specification<User> specification = Specification.where(UserSpecifications.withAppType(appType));
        if (StringUtils.isEmpty(search)) {
            log.info("fetching all users");
            return userRepository.findAll(specification, pageable);
        }
        log.info("fetching users based on the search criteria:{}", search);
        return userRepository.findAll(specification.and(UserSpecifications.withSearch(search)), pageable);
    }
    private List<UserRole> getUserRoles(User user, List<String> roles) {
        List<AppRole> appRolesByCode = appRoleDao.getAppRolesByCode(roles);
        List<UserRole> userRoles = new ArrayList<>();
        for(AppRole role : appRolesByCode) {
            UserRole userRole = userRoleRepository.findByUserIdAndRoleId(user.getId(), role.getId());
            if (ObjectUtils.isNotEmpty(userRole)) {
                userRoles.add(userRole);
            } else {
                userRole = new UserRole();
                userRole.setUser(user);
                userRole.setRole(role);
                userRoles.add(userRole);
            }
        }
        return userRoles;
    }

    private List<UserGroup> getUserGroups(User user, List<String> groups) {
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

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Page<UserBasicInfoResponse> findGroupPostResponseByIdIn(UUID groupId, Pageable pageable) {
        return userRepository.findActiveUserOfGroup(Status.ACTIVE.getValue(), groupId, pageable);
    }

    public Page<User> findAll(String appType, String search, UserFilterRequest userFilterRequest, Pageable pageable) {
        Specification<User> specification = buildSpecification(appType, userFilterRequest);
        if (StringUtils.isEmpty(search)) {
            log.info("fetching all users");
            return userRepository.findAll(specification, pageable);
        }
        log.info("fetching users based on the search criteria:{}", search);
        return userRepository.findAll(specification.and(UserSpecifications.withSearch(search)), pageable);
    }
    public Specification<User> buildSpecification(String appType, UserFilterRequest userFilterRequest) {
        Specification<User> specification = Specification.where(UserSpecifications.withAppType(appType));
        if (CollectionUtils.isNotEmpty(userFilterRequest.getUserGroups())) {
            specification = specification.and(UserSpecifications.hasUserGroups(userFilterRequest.getUserGroups()));
        }
        if (CollectionUtils.isNotEmpty(userFilterRequest.getUserRoles())) {
            specification = specification.and(UserSpecifications.hasUserRoles(userFilterRequest.getUserRoles()));
        }
        if(StringUtils.isNotEmpty(userFilterRequest.getStatus())){
            specification = specification.and(UserSpecifications.withStatus(userFilterRequest.getStatus()));
        }
        return specification;
    }

    public UserContactsResponse getUserContacts(UUID userId, Pageable pageable) {
        return userContactJDBCRepository.findUserContacts(userId, pageable);
    }

    public void saveUserContacts(UUID userId, List<UUID> newContactIds) {
        List<UserContact> userContacts = new ArrayList<>();
        userContactRepository.findByUserId(userId).stream().filter(userContact -> newContactIds.contains(userContact.getUserContactId())).forEach(userContact -> newContactIds.remove(userContact.getUserContactId()));
        for(UUID contactUserId : newContactIds){
            UserContact uc = new UserContact();
            uc.setUserId(userId);
            uc.setUserContactId(contactUserId);
            userContacts.add(uc);
        }
        userContactRepository.saveAll(userContacts);
    }

    public void removeUserContacts(UUID userId, List<UUID> removeContactIds) {
        List<UserContact> userContacts = userContactRepository.findByUserIdAndUserContactIdIn(userId, removeContactIds);
        userContactRepository.deleteAll(userContacts);
    }

    public void deleteEntityById(UUID userId) {
        loginAuditRepository.deleteEntityById(userId);
    }
}
