package com.fpt.ojt.repositories.condition;

import com.fpt.ojt.models.postgres.condition.ConditionOption;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionOptionRepository
        extends JpaRepository<ConditionOption, UUID>, JpaSpecificationExecutor<ConditionOption> {}
