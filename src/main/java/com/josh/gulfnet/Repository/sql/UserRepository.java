package com.josh.gulfnet.Repository.sql;

import com.josh.gulfnet.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsernameAndPassword(String userName, String password);

}
