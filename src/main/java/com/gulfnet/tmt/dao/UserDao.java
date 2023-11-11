package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.model.User;
import com.gulfnet.tmt.repository.sql.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserDao {
    private final UserRepository userRepository;

    public UserDao(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
