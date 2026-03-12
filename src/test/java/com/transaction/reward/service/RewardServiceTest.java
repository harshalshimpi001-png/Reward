package com.transaction.reward.service;

import com.transaction.reward.dto.RewardResponseDTO;
import com.transaction.reward.entity.Customer;
import com.transaction.reward.entity.CustomerTxn;
import com.transaction.reward.exception.InvalidDateRangeException;
import com.transaction.reward.exception.ResourceNotFoundException;
import com.transaction.reward.repository.CustomerRepository;
import com.transaction.reward.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RewardService rewardService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Harshal");
    }

    @Test
    void testCalculateRewards_Success() {

        CustomerTxn txn1 = new CustomerTxn();
        txn1.setTxnAmount(BigDecimal.valueOf(120.0));
        txn1.setTxnDate(LocalDate.of(2025,1,10));

        CustomerTxn txn2 = new CustomerTxn();
        txn2.setTxnAmount(BigDecimal.valueOf(80.0));
        txn2.setTxnDate(LocalDate.of(2025,2,15));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndTxnDateBetween(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(txn1, txn2));

        RewardResponseDTO response = rewardService.calculateRewards(
                1L,
                LocalDate.of(2025,1,1),
                LocalDate.of(2025,3,1)
        );

        assertNotNull(response);
        assertEquals(1L, response.getCustomerId());
        assertEquals("Harshal", response.getCustomerName());
        assertEquals(120, response.getTotalRewardPoints());

        assertEquals(2, response.getMonthlyRewards().size());
        assertEquals(2025, response.getMonthlyRewards().get(0).getYear());
        assertEquals(1, response.getMonthlyRewards().get(0).getMonth());
        assertEquals(90, response.getMonthlyRewards().get(0).getRewardPoints());
        assertEquals(2025, response.getMonthlyRewards().get(1).getYear());
        assertEquals(2, response.getMonthlyRewards().get(1).getMonth());
        assertEquals(30, response.getMonthlyRewards().get(1).getRewardPoints());
    }

    @Test
    void testCustomerNotFound() {

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                rewardService.calculateRewards(
                        1L,
                        LocalDate.now().minusMonths(3),
                        LocalDate.now()
                )
        );
    }

    @Test
    void testInvalidDateRange() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(InvalidDateRangeException.class, () ->
                rewardService.calculateRewards(
                        1L,
                        LocalDate.of(2025,3,1),
                        LocalDate.of(2025,1,1)
                )
        );
    }

    @Test
    void testNoTransactions() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        when(transactionRepository.findByCustomerIdAndTxnDateBetween(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        RewardResponseDTO response = rewardService.calculateRewards(
                1L,
                LocalDate.now().minusMonths(3),
                LocalDate.now()
        );

        assertEquals(0, response.getTotalRewardPoints());
        assertTrue(response.getMonthlyRewards().isEmpty());
    }

    @Test
    void testMultipleMonthsTransactions() {

        CustomerTxn txn1 = new CustomerTxn();
        txn1.setTxnAmount(BigDecimal.valueOf(120.0));
        txn1.setTxnDate(LocalDate.of(2025,1,10));

        CustomerTxn txn2 = new CustomerTxn();
        txn2.setTxnAmount(BigDecimal.valueOf(200.0));
        txn2.setTxnDate(LocalDate.of(2025,2,10));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(transactionRepository.findByCustomerIdAndTxnDateBetween(anyLong(), any(), any()))
                .thenReturn(Arrays.asList(txn1, txn2));

        RewardResponseDTO response = rewardService.calculateRewards(
                1L,
                LocalDate.of(2025,1,1),
                LocalDate.of(2025,3,1)
        );

        assertEquals(2, response.getMonthlyRewards().size());
    }

}
