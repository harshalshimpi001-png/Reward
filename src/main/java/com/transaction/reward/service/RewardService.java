package com.transaction.reward.service;

import com.transaction.reward.dto.MonthlyRewardDTO;
import com.transaction.reward.dto.RewardResponseDTO;
import com.transaction.reward.entity.Customer;
import com.transaction.reward.entity.CustomerTxn;
import com.transaction.reward.exception.InvalidDateRangeException;
import com.transaction.reward.exception.ResourceNotFoundException;
import com.transaction.reward.repository.CustomerRepository;
import com.transaction.reward.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RewardService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public RewardService(TransactionRepository transactionRepository,
                         CustomerRepository customerRepository) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    public RewardResponseDTO calculateRewards(Long customerId, LocalDate startDate, LocalDate endDate){

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found, customerId : " + customerId));

        if(startDate != null && endDate != null && startDate.isAfter(endDate)){
            throw new InvalidDateRangeException("Start Date must be before end date, Start Date: "+ startDate + " End Date: "+ endDate);
        }

        List<CustomerTxn> transactions = transactionRepository.findByCustomerIdAndTxnDateBetween(customerId, startDate, endDate);

        Map<String, List<CustomerTxn>> groupedByMonthYearTxn = transactions.stream()
                .collect(Collectors.groupingBy(txn ->
                        txn.getTxnDate().getYear() + "-" + txn.getTxnDate().getMonthValue()
                ));

        List<MonthlyRewardDTO> monthlyRewards = new ArrayList<>();
        int totalPoints = this.getTotalPointsAndMonthlyRewardsList(groupedByMonthYearTxn, monthlyRewards);

        RewardResponseDTO response = new RewardResponseDTO();
        response.setCustomerId(customer.getId());
        response.setCustomerName(customer.getName());
        response.setMonthlyRewards(monthlyRewards);
        response.setTotalRewardPoints(totalPoints);

        return response;
    }

    private int getTotalPointsAndMonthlyRewardsList(Map<String, List<CustomerTxn>> groupedByMonthYearTxn, List<MonthlyRewardDTO> monthlyRewards) {
        int totalPoints = 0;

        for (Map.Entry<String, List<CustomerTxn>> entry : groupedByMonthYearTxn.entrySet()) {
            String[] monthYear = entry.getKey().split("-");
            int year = Integer.parseInt(monthYear[0]);
            int month = Integer.parseInt(monthYear[1]);

            int monthlyPoints = entry.getValue().stream()
                    .mapToInt(txn -> txn.getTxnAmount() == null ? 0
                            : RewardUtil.calculateRewardPoints(txn.getTxnAmount()))
                    .sum();

            totalPoints += monthlyPoints;

            MonthlyRewardDTO dtoMonthly = new MonthlyRewardDTO(month, year, monthlyPoints);
            monthlyRewards.add(dtoMonthly);
        }
        monthlyRewards.sort(Comparator
                .comparing(MonthlyRewardDTO::getYear)
                .thenComparing(MonthlyRewardDTO::getMonth));
        return totalPoints;
    }

}
