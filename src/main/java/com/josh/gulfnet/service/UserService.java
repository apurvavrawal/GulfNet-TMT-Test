package com.josh.gulfnet.service;

import com.josh.gulfnet.dao.UserDao;
import com.josh.gulfnet.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User getUserDetails(String userId) {
        return userDao.getUserDetails(Integer.parseInt(userId));
    }

    public User createUser(User user) {
        return userDao.createUser(user);
    }

    public User getUserByNameAndPassword(String username, String password){
        return userDao.getAuthenticatedUser(username, password);
    }
}
