package com.fpt.ojt.repositories.user;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fpt.ojt.models.postgres.card.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, UUID> {
    boolean existsByPhoneCode(String phoneCode);
}
