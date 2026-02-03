package com.fpt.ojt.services.card;

import com.fpt.ojt.presentations.dtos.requests.card.AddCardToUserRequest;
import com.fpt.ojt.presentations.dtos.requests.card.EditUserCard;
import com.fpt.ojt.services.dtos.AvailableCardRulesDto;
import com.fpt.ojt.services.dtos.CardProductDto;
import com.fpt.ojt.services.dtos.UserCardDto;

import java.util.List;
import java.util.UUID;

public interface CardService {
    List<AvailableCardRulesDto> getAvailableCardRulesByUserId(UUID userId);

    List<CardProductDto> searchCardProducts(String keyword, int limit);

    List<UserCardDto> getUserCards(UUID userId);

    void addCardToUser(UUID userId, AddCardToUserRequest userCardDto);

    void editUserCard(UUID userCardId, UUID userId, EditUserCard userCardDto);

    void removeUserCard(UUID userCardId, UUID userId);
}
