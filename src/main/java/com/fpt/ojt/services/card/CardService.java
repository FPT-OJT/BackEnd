package com.fpt.ojt.services.card;

import com.fpt.ojt.services.dtos.AvailableCardRulesDto;
import com.fpt.ojt.services.dtos.CardProductDto;

import java.util.List;
import java.util.UUID;


public interface CardService {
    List<AvailableCardRulesDto> getAvailableCardRulesByUserId(UUID userId);
    List<CardProductDto> searchCardProducts(String keyword,int limit);
}
