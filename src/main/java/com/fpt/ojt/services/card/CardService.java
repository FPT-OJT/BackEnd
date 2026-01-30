package com.fpt.ojt.services.card;

import com.fpt.ojt.services.dtos.AvailableCardRulesDto;

import java.util.List;
import java.util.UUID;

public interface CardService {
    List<AvailableCardRulesDto> getAvailableCardRulesByUserId(UUID userId);
}
