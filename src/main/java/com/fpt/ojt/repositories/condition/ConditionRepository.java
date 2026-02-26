package com.fpt.ojt.repositories.condition;

import com.fpt.ojt.models.postgres.condition.Condition;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, UUID>, JpaSpecificationExecutor<Condition> {}
