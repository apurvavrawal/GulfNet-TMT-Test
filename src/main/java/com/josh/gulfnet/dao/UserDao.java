package com.josh.gulfnet.dao;

import com.josh.gulfnet.model.User;
import com.josh.gulfnet.Repository.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    @Autowired
    private UserRepository userRepository;

    public User getUserDetails(Integer userId) {
        return userRepository.findById(userId).get();
    }

    public User getAuthenticatedUser(String username, String password){
        return userRepository.findByUsernameAndPassword(username, password);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }
}
