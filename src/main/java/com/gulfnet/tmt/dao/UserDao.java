package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.repository.sql.UserRepository;
import com.gulfnet.tmt.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserDao {

    private final UserRepository userRepository;
    public User getAuthenticatedUser(String username, String password) {
        log.info("UserName : {}, password : {}", username, EncryptionUtil.encrypt(password));
        return userRepository.findByUserNameAndPassword(username, EncryptionUtil.encrypt(password));
    }
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

}
