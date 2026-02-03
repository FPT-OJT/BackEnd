package com.fpt.ojt.services.card.impl;

import com.fpt.ojt.exceptions.ForbiddenException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.exceptions.QueryErrorException;
import com.fpt.ojt.models.postgres.card.CardProduct;
import com.fpt.ojt.models.postgres.card.CardRule;
import com.fpt.ojt.models.postgres.card.UserCreditCard;
import com.fpt.ojt.presentations.dtos.requests.card.AddCardToUserRequest;
import com.fpt.ojt.presentations.dtos.requests.card.EditUserCard;
import com.fpt.ojt.repositories.card.CardProductRepository;
import com.fpt.ojt.repositories.card.CardRuleRepository;
import com.fpt.ojt.repositories.card.UserCreditCardRepository;
import com.fpt.ojt.services.card.CardService;
import com.fpt.ojt.services.dtos.AvailableCardRulesDto;
import com.fpt.ojt.services.dtos.CardProductDto;
import com.fpt.ojt.services.dtos.UserCardDto;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRuleRepository cardRuleRepository;
    private final CardProductRepository cardProductRepository;
    private final UserCreditCardRepository userCreditCardRepository;
    private final EntityManager entityManager;

    @Override
    public List<AvailableCardRulesDto> getAvailableCardRulesByUserId(UUID userId) {
        try {
            List<CardRule> cardRules = cardRuleRepository.findAllAvailableByCardRulesByUserId(userId);

            Map<UUID, List<CardRule>> groupedByProduct = cardRules.stream()
                    .collect(Collectors.groupingBy(rule -> rule.getCardProduct().getId()));

            return groupedByProduct.entrySet().stream()
                    .map(entry -> AvailableCardRulesDto.builder()
                            .cardProductId(entry.getKey())
                            .cardRules(entry.getValue().stream()
                                    .map(this::mapToCardRulesDto)
                                    .toList())
                            .build())
                    .toList();
        } catch (RuntimeException e) {
            log.error("Error fetching available card rules for user: {}", userId, e);
            throw new QueryErrorException("Failed to fetch available card rules: " + e.getMessage());
        }
    }

    private AvailableCardRulesDto.CardRulesDto mapToCardRulesDto(CardRule cardRule) {
        AvailableCardRulesDto.CardRulesDto dto = new AvailableCardRulesDto.CardRulesDto();
        dto.setAllowMccs(cardRule.getMatchAllowMccs());
        dto.setRejectMccs(cardRule.getMatchRejectMccs());
        dto.setEffectCashbackRate(cardRule.getEffectCashbackRate());
        dto.setEffectRebateRate(cardRule.getEffectRebateRate());
        dto.setEffectMerchantDiscountRate(cardRule.getEffectMerchantDiscountRate());
        dto.setEffectFeeRate(cardRule.getEffectFeeRate());
        return dto;
    }

    @Override
    public List<CardProductDto> searchCardProducts(String keyword, int limit) {
        var cardProducts = cardProductRepository.search(keyword, limit);
        return cardProducts.stream()
                .map(this::mapToCardProductDto)
                .toList();
    }

    private CardProductDto mapToCardProductDto(com.fpt.ojt.models.postgres.card.CardProduct cardProduct) {
        return CardProductDto.builder()
                .cardName(cardProduct.getCardName())
                .cardType(cardProduct.getCardType())
                .imageUrl(cardProduct.getImageUrl())
                .cardCode(cardProduct.getCardCode())
                .build();
    }

    @Override
    public List<UserCardDto> getUserCards(UUID userId) {
        var userCards = userCreditCardRepository.findByUserId(userId);
        return userCards.stream()
                .map(UserCardDto::fromEntity)
                .toList();

    }

    @Override
    public void addCardToUser(UUID userId, AddCardToUserRequest request) {
        var cardProduct = cardProductRepository.findById(request.getCardId())
                .orElseThrow(() -> new NotFoundException("Card product not found with id: " + request.getCardId()));
        var user = entityManager.getReference(com.fpt.ojt.models.postgres.user.User.class, userId);

        var userCard = UserCreditCard.builder()
                .firstPaymentDate(request.getFirstPaymentDate())
                .expiryDate(request.getExpiryDate())
                .cardProduct(cardProduct)
                .user(user)
                .build();
        userCreditCardRepository.save(userCard);
    }

    @Override
    public void editUserCard(UUID userCardId, UUID userId, EditUserCard userCardDto) {
        var userCard = userCreditCardRepository.findById(userCardId)
                .orElseThrow(() -> new NotFoundException("User card not found with id: " + userCardId));
        if (!userCard.getUser().getId().equals(userId)) {
            throw new ForbiddenException("User does not own this card");
        }
        userCard.setFirstPaymentDate(userCardDto.getFirstPaymentDate());
        userCard.setExpiryDate(userCardDto.getExpiryDate());
        userCreditCardRepository.save(userCard);
    }
}
