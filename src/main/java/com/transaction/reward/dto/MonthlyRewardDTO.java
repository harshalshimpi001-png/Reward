package com.transaction.reward.dto;

public class MonthlyRewardDTO {

    private int month;
    private int year;
    private int rewardPoints;

    public MonthlyRewardDTO(int month, int year, int rewardPoints) {
        this.month = month;
        this.year = year;
        this.rewardPoints = rewardPoints;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(int rewardPoints) {
        this.rewardPoints = rewardPoints;
    }
}
