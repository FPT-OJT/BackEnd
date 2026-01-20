package com.fpt.ojt.repositories;

import com.fpt.ojt.models.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PasswordResetTokenRepository  extends CrudRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByUserId(UUID userId);

    void deleteAllByUserId(UUID userId);
}
