package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsernameAndPassword(String userName, String password);

}
