package com.fpt.ojt.repositories.user;

import com.fpt.ojt.models.postgres.card.Country;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    boolean existsByPhoneCode(String phoneCode);
}
