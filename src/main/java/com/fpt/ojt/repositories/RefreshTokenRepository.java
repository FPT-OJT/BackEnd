package com.fpt.ojt.repositories;

import com.fpt.ojt.models.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
    RefreshToken findByUserId(UUID userId);

    List<RefreshToken> findAllByFamilyToken(String familyToken);

    List<RefreshToken> findByRefreshTokenAndFamilyToken(String refreshToken, String familyToken);

    List<RefreshToken> findAllByRefreshTokenAndFamilyTokenAndIsRevoked(String refreshToken, String familyToken, boolean isRevoked);

    List<RefreshToken> findAllByFamilyTokenAndUserIdAndIsRevoked(String familyToken, UUID userId, boolean isRevoked);

    List<RefreshToken> findAllByFamilyTokenAndIsRevoked(String familyToken, boolean isRevoked);
}
