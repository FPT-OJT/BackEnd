package com.fpt.ojt.services.card.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fpt.ojt.exceptions.ForbiddenException;
import com.fpt.ojt.exceptions.NotFoundException;
import com.fpt.ojt.models.enums.EnumConstants;
import com.fpt.ojt.models.postgres.card.CardProduct;
import com.fpt.ojt.models.postgres.card.UserCreditCard;
import com.fpt.ojt.models.postgres.user.User;
import com.fpt.ojt.presentations.dtos.requests.card.EditUserCard;
import com.fpt.ojt.repositories.card.CardProductRepository;
import com.fpt.ojt.repositories.card.CardRuleRepository;
import com.fpt.ojt.repositories.card.UserCreditCardRepository;
import com.fpt.ojt.services.dtos.CardProductDto;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @Mock
    private CardRuleRepository cardRuleRepository;

    @Mock
    private CardProductRepository cardProductRepository;

    @Mock
    private UserCreditCardRepository userCreditCardRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CardServiceImpl cardService;

    // ── searchCardProducts ───────────────────────────────────────────────────────

    @Test
    void searchCardProducts_shortKeyword_callsSearchShort() {
        // keyword length < 4 (MIN_SEARCH_LENGTH)
        String keyword = "abc";
        when(cardProductRepository.searchShort(keyword, 5)).thenReturn(List.of());

        List<CardProductDto> result = cardService.searchCardProducts(keyword, 5);

        verify(cardProductRepository).searchShort(keyword, 5);
        verify(cardProductRepository, never()).searchLong(any(), anyInt());
        assertThat(result).isEmpty();
    }

    @Test
    void searchCardProducts_longKeyword_callsSearchLong() {
        // keyword length >= 4 (MIN_SEARCH_LENGTH)
        String keyword = "visa";
        when(cardProductRepository.searchLong(keyword, 10)).thenReturn(List.of());

        List<CardProductDto> result = cardService.searchCardProducts(keyword, 10);

        verify(cardProductRepository).searchLong(keyword, 10);
        verify(cardProductRepository, never()).searchShort(any(), anyInt());
        assertThat(result).isEmpty();
    }

    // ── isUserCardExists ─────────────────────────────────────────────────────────

    @Test
    void isUserCardExists_returnsTrue_whenExists() {
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(userCreditCardRepository.existsByCardProductIdAndUserIdAndDeletedAtIsNull(cardId, userId))
                .thenReturn(true);

        boolean result = cardService.isUserCardExists(cardId, userId);

        assertThat(result).isTrue();
    }

    @Test
    void isUserCardExists_returnsFalse_whenNotExists() {
        UUID cardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(userCreditCardRepository.existsByCardProductIdAndUserIdAndDeletedAtIsNull(cardId, userId))
                .thenReturn(false);

        boolean result = cardService.isUserCardExists(cardId, userId);

        assertThat(result).isFalse();
    }

    // ── isUserCardEmpty ─────────────────────────────────────────────────────────

    @Test
    void isUserCardEmpty_returnsTrue_whenNoCards() {
        UUID userId = UUID.randomUUID();
        when(userCreditCardRepository.existsByUserIdAndDeletedAtIsNull(userId)).thenReturn(false);

        boolean result = cardService.isUserCardEmpty(userId);

        assertThat(result).isTrue();
    }

    @Test
    void isUserCardEmpty_returnsFalse_whenCardsExist() {
        UUID userId = UUID.randomUUID();
        when(userCreditCardRepository.existsByUserIdAndDeletedAtIsNull(userId)).thenReturn(true);

        boolean result = cardService.isUserCardEmpty(userId);

        assertThat(result).isFalse();
    }

    // ── editUserCard ─────────────────────────────────────────────────────────────

    @Test
    void editUserCard_cardNotFound_throwsNotFoundException() {
        UUID userCardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(userCreditCardRepository.findById(userCardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.editUserCard(userCardId, userId, new EditUserCard()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void editUserCard_userDoesNotOwnCard_throwsForbiddenException() {
        UUID userCardId = UUID.randomUUID();
        UUID ownerId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        User owner = User.builder()
                .role(EnumConstants.RoleEnum.CUSTOMER)
                .firstName("Owner")
                .lastName("User")
                .build();
        // Set the owner's ID via reflection helper (AbstractBaseEntity uses
        // Hibernate-generated ID)
        // We use a different UUID so the ownership check fails
        UserCreditCard card = UserCreditCard.builder()
                .user(owner)
                .cardProduct(mock(CardProduct.class))
                .build();

        when(userCreditCardRepository.findById(userCardId)).thenReturn(Optional.of(card));

        // owner.getId() returns null by default (no persistence context), which won't
        // equal otherUserId
        assertThatThrownBy(() -> cardService.editUserCard(userCardId, otherUserId, new EditUserCard()))
                .isInstanceOf(ForbiddenException.class);
    }

    // ── removeUserCard ───────────────────────────────────────────────────────────

    @Test
    void removeUserCard_cardNotFound_throwsNotFoundException() {
        UUID userCardId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(userCreditCardRepository.findById(userCardId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cardService.removeUserCard(userCardId, userId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void removeUserCard_userDoesNotOwnCard_throwsForbiddenException() {
        UUID userCardId = UUID.randomUUID();
        UUID otherUserId = UUID.randomUUID();

        User owner = User.builder()
                .role(EnumConstants.RoleEnum.CUSTOMER)
                .firstName("Owner")
                .lastName("User")
                .build();
        UserCreditCard card = UserCreditCard.builder()
                .user(owner)
                .cardProduct(mock(CardProduct.class))
                .build();

        when(userCreditCardRepository.findById(userCardId)).thenReturn(Optional.of(card));

        assertThatThrownBy(() -> cardService.removeUserCard(userCardId, otherUserId))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void removeUserCard_success_setsDeletedAt() {
        UUID userCardId = UUID.randomUUID();

        User owner = User.builder()
                .role(EnumConstants.RoleEnum.CUSTOMER)
                .firstName("Owner")
                .lastName("User")
                .build();
        UserCreditCard card = UserCreditCard.builder()
                .user(owner)
                .cardProduct(mock(CardProduct.class))
                .firstPaymentDate(1)
                .expiryDate(LocalDate.now().plusYears(2))
                .build();

        // owner.getId() returns null - pass null as userId so ownership check passes
        when(userCreditCardRepository.findById(userCardId)).thenReturn(Optional.of(card));

        // null == null, so ownership check passes
        cardService.removeUserCard(userCardId, null);

        assertThat(card.getDeletedAt()).isNotNull();
        verify(userCreditCardRepository).save(card);
    }
}
