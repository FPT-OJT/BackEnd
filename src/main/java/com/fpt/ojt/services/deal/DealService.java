package com.fpt.ojt.services.deal;

import com.fpt.ojt.services.dtos.AvailableCardRulesDto;
import com.fpt.ojt.services.dtos.MerchantAgencyWithAvailableDealsDto;

import java.util.List;

public interface DealService {
    List<MerchantAgencyWithAvailableDealsDto> getAvailableMerchantDeals();

    Double calculateMerchantOfferOnMerchantDealAndUserCardList(
            String mcc,
            MerchantAgencyWithAvailableDealsDto.MerchantDealItem merchantDealItem,
            List<AvailableCardRulesDto> userCardList
    );

    Double calculateCardOnlyBenefit(AvailableCardRulesDto.CardRulesDto cardRule);
}
