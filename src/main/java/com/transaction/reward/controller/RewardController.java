package com.transaction.reward.controller;

import com.transaction.reward.dto.RewardResponseDTO;
import com.transaction.reward.service.RewardService;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/rewards")
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<RewardResponseDTO> getRewards(
            @PathVariable @NotNull Long customerId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ){
        LocalDate defEndDate = (endDate != null) ? LocalDate.parse(endDate) : LocalDate.now();
        LocalDate defStartDate = (startDate != null) ? LocalDate.parse(startDate) : defEndDate.minusMonths(3);

        return ResponseEntity.ok(rewardService.calculateRewards(customerId, defStartDate, defEndDate));
    }
}
