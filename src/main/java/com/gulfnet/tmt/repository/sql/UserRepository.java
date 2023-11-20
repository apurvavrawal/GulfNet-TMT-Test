package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUserNameAndPassword(String userName, String password);

    Optional<User> findByUserName(String userName);

}
