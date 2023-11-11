package com.gulfnet.tmt.service;

import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserDetails(String userId) {
        return userDao.getUserDetails(Integer.parseInt(userId));
    }

    public User saveUser(User user) {
        return userDao.createUser(user);
    }

    public User getUserByNameAndPassword(String username, String password){
        return userDao.getAuthenticatedUser(username, password);
    }
}
