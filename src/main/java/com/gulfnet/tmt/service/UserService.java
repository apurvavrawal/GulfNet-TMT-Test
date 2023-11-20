package com.gulfnet.tmt.service;

import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User saveUser(User user) {
        return userDao.saveUser(user);
    }

    public Optional<User> getUserByUserName(String userName) {
        return userDao.getUserByUserName(userName);
    }


}
