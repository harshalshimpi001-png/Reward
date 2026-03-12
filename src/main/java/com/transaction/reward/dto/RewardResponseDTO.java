package com.transaction.reward.dto;

import java.util.List;

public class RewardResponseDTO {

    private Long customerId;
    private String customerName;
    private List<MonthlyRewardDTO> monthlyRewards;
    private int totalRewardPoints;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getTotalRewardPoints() {
        return totalRewardPoints;
    }

    public void setTotalRewardPoints(int totalRewardPoints) {
        this.totalRewardPoints = totalRewardPoints;
    }

    public List<MonthlyRewardDTO> getMonthlyRewards() {
        return monthlyRewards;
    }

    public void setMonthlyRewards(List<MonthlyRewardDTO> monthlyRewards) {
        this.monthlyRewards = monthlyRewards;
    }
}
