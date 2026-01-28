package com.fpt.ojt.repositories;

import com.fpt.ojt.models.postgres.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    User findByUserName(String userName);

    User findByGoogleId(String googleId);

    List<User> findAllByGoogleIdOrEmail(String googleId, String email);

    User findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);
}
