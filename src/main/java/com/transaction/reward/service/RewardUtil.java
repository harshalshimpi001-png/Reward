package com.transaction.reward.service;

import java.math.BigDecimal;

public class RewardUtil {

    public static int calculateRewardPoints(BigDecimal amount){
        if (amount == null) {
            return 0;
        }
        int points = 0;

        if(amount.compareTo(BigDecimal.valueOf(100)) > 0){
            points += (amount.subtract(BigDecimal.valueOf(100)).intValue()) * 2;
            points += 50;
        } else if(amount.compareTo(BigDecimal.valueOf(50)) > 0){
            points += amount.subtract(BigDecimal.valueOf(50)).intValue();
        }
        return points;
    }
}
