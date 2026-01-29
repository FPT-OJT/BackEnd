package com.fpt.ojt.presentations.dtos.responses.home;

import com.fpt.ojt.presentations.dtos.responses.AbstractBaseResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class HomePageResponse extends AbstractBaseResponse {
    private List<MerchantCategory> merchantCategories;
    private List<MerchantOffer> merchantOffers;

    @Getter
    @Setter
    @Builder
    public static class MerchantOffer {
        private UUID merchantId;
        private String merchantName;
        private String imageUrl;
        private String merchantDealName;
        private boolean isFavorite;
        private boolean isSubscribed;
        private Double distance;
        private Double totalDiscount;
    }

    @Getter
    @Setter
    @Builder
    public static class MerchantCategory {
        private UUID id;
        private String name;
        private String imageUrl;
    }
}
