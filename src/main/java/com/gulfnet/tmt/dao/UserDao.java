package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.AppRole;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.entity.sql.UserGroup;
import com.gulfnet.tmt.entity.sql.UserRole;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.response.GroupUserResponse;
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
    private final AppRoleDao appRoleDao;
    private final GroupDao groupDao;
    private static final String key = "4995f5e3-0280-4e6a-ad40-917136cbb884";
    public User getAuthenticatedUser(String username, String password, String appType) {
        log.info("UserName : {}, password : {}", username, EncryptionUtil.encrypt(password, key));
        return userRepository.findByUserNameAndPasswordAndAppType(username, EncryptionUtil.encrypt(password, key), appType);
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
        }
        user = userRepository.save(user);
        return user;
    }
    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
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

    public Page<GroupUserResponse> findGroupPostResponseByIdIn(UUID groupId, Pageable pageable) {
        return userRepository.findActiveUserOfGroup(Status.ACTIVE.getValue(), groupId, pageable);
    }
}
