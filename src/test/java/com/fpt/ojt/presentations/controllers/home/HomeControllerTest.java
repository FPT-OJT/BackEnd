package com.fpt.ojt.presentations.controllers.home;

import com.fpt.ojt.infrastructure.securities.JwtAuthenticationFilter;
import com.fpt.ojt.infrastructure.securities.JwtTokenProvider;
import com.fpt.ojt.presentations.controllers.base.ResponseFactory;
import com.fpt.ojt.presentations.dtos.responses.home.HomePageResponse;
import com.fpt.ojt.services.merchants.MerchantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HomeController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ResponseFactory.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MerchantService merchantService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private HomePageResponse homePageResponse;

    @BeforeEach
    void setUp() {
        HomePageResponse.MerchantCategory category = HomePageResponse.MerchantCategory.builder()
                .id(UUID.randomUUID())
                .name("Food & Beverage")
                .imageUrl("https://example.com/food.png")
                .build();

        HomePageResponse.MerchantOffer offer = HomePageResponse.MerchantOffer.builder()
                .merchantAgencyId(UUID.randomUUID())
                .merchantAgencyName("Test Restaurant")
                .imageUrl("https://example.com/restaurant.png")
                .merchantDealName("50% Off Lunch")
                .isFavorite(true)
                .isSubscribed(false)
                .lat(10.762622)
                .lng(106.660172)
                .totalDiscount(50.0)
                .build();

        homePageResponse = HomePageResponse.builder()
                .merchantCategories(List.of(category))
                .merchantOffers(List.of(offer))
                .build();
    }

    @Nested
    @DisplayName("GET /home")
    class GetHomePageTests {

        @Test
        @DisplayName("Should return homepage data successfully")
        void getHomePage_Success() throws Exception {
            when(merchantService.getHomePage()).thenReturn(homePageResponse);

            mockMvc.perform(get("/home"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("Retrieve Home page successfully"))
                    .andExpect(jsonPath("$.data.merchantCategories").isArray())
                    .andExpect(jsonPath("$.data.merchantCategories[0].name").value("Food & Beverage"))
                    .andExpect(jsonPath("$.data.merchantOffers").isArray())
                    .andExpect(jsonPath("$.data.merchantOffers[0].merchantAgencyName").value("Test Restaurant"))
                    .andExpect(jsonPath("$.data.merchantOffers[0].merchantDealName").value("50% Off Lunch"))
                    .andExpect(jsonPath("$.data.merchantOffers[0].totalDiscount").value(50.0));

            verify(merchantService, times(1)).getHomePage();
        }

        @Test
        @DisplayName("Should return empty lists when no data available")
        void getHomePage_EmptyData_Success() throws Exception {
            HomePageResponse emptyResponse = HomePageResponse.builder()
                    .merchantCategories(Collections.emptyList())
                    .merchantOffers(Collections.emptyList())
                    .build();
            when(merchantService.getHomePage()).thenReturn(emptyResponse);

            mockMvc.perform(get("/home"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.statusCode").value(200))
                    .andExpect(jsonPath("$.message").value("Retrieve Home page successfully"))
                    .andExpect(jsonPath("$.data.merchantCategories").isEmpty())
                    .andExpect(jsonPath("$.data.merchantOffers").isEmpty());

            verify(merchantService, times(1)).getHomePage();
        }

        @Test
        @DisplayName("Should return 500 when service throws ExecutionException")
        void getHomePage_ExecutionException_InternalServerError() throws Exception {
            when(merchantService.getHomePage()).thenThrow(new ExecutionException("Async error", new RuntimeException()));

            mockMvc.perform(get("/home"))
                    .andExpect(status().isInternalServerError());

            verify(merchantService, times(1)).getHomePage();
        }

        @Test
        @DisplayName("Should return 500 when service throws InterruptedException")
        void getHomePage_InterruptedException_InternalServerError() throws Exception {
            when(merchantService.getHomePage()).thenThrow(new InterruptedException("Thread interrupted"));

            mockMvc.perform(get("/home"))
                    .andExpect(status().isInternalServerError());

            verify(merchantService, times(1)).getHomePage();
        }
    }
}
