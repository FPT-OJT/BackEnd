package com.fpt.ojt.repositories.condition;

import com.fpt.ojt.models.postgres.condition.Condition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, UUID>, JpaSpecificationExecutor<Condition> {
}
