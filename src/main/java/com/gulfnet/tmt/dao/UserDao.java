package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.AppRole;
import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.entity.sql.UserRole;
import com.gulfnet.tmt.repository.sql.UserRepository;
import com.gulfnet.tmt.repository.sql.UserRoleRepository;
import com.gulfnet.tmt.util.EncryptionUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final AppRoleDao appRoleDao;
    public User getAuthenticatedUser(String username, String password, String appType) {
        log.info("UserName : {}, password : {}", username, EncryptionUtil.encrypt(password));
        return userRepository.findByUserNameAndPasswordAndAppType(username, EncryptionUtil.encrypt(password), appType);
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUser(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public User saveUser(User user, List<String> role) {
        user = userRepository.save(user);
        List<UserRole> userRoles = getUserRoles(user, role);
        user.setUserRole(userRoleRepository.saveAll(userRoles));
        return user;
    }

    private List<UserRole> getUserRoles(User user, List<String> role) {
        List<AppRole> appRolesByCode = appRoleDao.getAppRolesByCode(role);
        List<UserRole> userRoles = new ArrayList<>();
        for(AppRole appRole:appRolesByCode){
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setRole(appRole);
            userRoles.add(userRole);
        }
        return userRoles;
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
