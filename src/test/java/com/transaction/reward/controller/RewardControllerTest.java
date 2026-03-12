package com.transaction.reward.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.reward.dto.MonthlyRewardDTO;
import com.transaction.reward.dto.RewardResponseDTO;
import com.transaction.reward.service.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    private RewardResponseDTO mockResponse() {

        MonthlyRewardDTO jan = new MonthlyRewardDTO(1,2025,90);
        MonthlyRewardDTO feb = new MonthlyRewardDTO(2,2025,30);

        RewardResponseDTO response = new RewardResponseDTO();
        response.setCustomerId(1L);
        response.setCustomerName("Harshal");
        response.setTotalRewardPoints(120);
        response.setMonthlyRewards(List.of(jan,feb));

        return response;
    }

    @Test
    void testGetRewards_WithDateRange_Success() throws Exception {

        RewardResponseDTO response = mockResponse();

        Mockito.when(rewardService.calculateRewards(
                eq(1L),
                eq(LocalDate.of(2025,1,1)),
                eq(LocalDate.of(2025,3,1))
        )).thenReturn(response);

        mockMvc.perform(get("/rewards/1")
                        .param("startDate","2025-01-01")
                        .param("endDate","2025-03-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("Harshal"))
                .andExpect(jsonPath("$.totalRewardPoints").value(120))
                .andExpect(jsonPath("$.monthlyRewards.length()").value(2));
    }


    @Test
    void testGetRewards_WithDefaultDates() throws Exception {

        RewardResponseDTO response = mockResponse();

        Mockito.when(rewardService.calculateRewards(
                eq(1L),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenReturn(response);

        mockMvc.perform(get("/rewards/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.totalRewardPoints").value(120));
    }

    @Test
    void testGetRewards_ServiceThrowsException() throws Exception {

        Mockito.when(rewardService.calculateRewards(
                eq(1L),
                any(LocalDate.class),
                any(LocalDate.class)
        )).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/rewards/1"))
                .andExpect(status().isInternalServerError());
    }
}