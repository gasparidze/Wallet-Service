package org.example.io.ylab.walletservicе.application_logic;

import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.TransactionException;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.Transaction;
import org.example.io.ylab.walletservicе.domain.TransactionTypeEnum;
import org.example.io.ylab.walletservicе.domain.dto.CreateTransactionDto;
import org.example.io.ylab.walletservicе.domain.mapper.TransactionMapper;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.TransactionRepositoryI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    private static final Long TEST_ACCOUNT_NUMBER = 123456789L;
    @Mock
    private PlayerAccountServiceI playerAccountService;
    @Mock
    private TransactionRepositoryI transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @InjectMocks
    private TransactionService transactionService;

    @Test
    void doDebit() {
        CreateTransactionDto createTransactionDto = getCreateTransactionDto(TransactionTypeEnum.DEBIT, 0);
        PlayerAccount playerAccount = getPlayerAccount();
        Transaction transaction = getTransaction();
        doReturn(Optional.of(playerAccount)).when(playerAccountService).getAccountByNumber(TEST_ACCOUNT_NUMBER);
        doReturn(transaction).when(transactionMapper).map(createTransactionDto);

        transactionService.doDebit(createTransactionDto);

        verify(transactionMapper).map(createTransactionDto);
        verify(transactionRepository).createTransaction(transaction);
    }

    @Test
    void doDebitExceptionScenario() {
        PlayerAccount playerAccount = getPlayerAccount();
        CreateTransactionDto createTransactionDto = getCreateTransactionDto(TransactionTypeEnum.DEBIT, 10);
        doReturn(Optional.of(playerAccount)).when(playerAccountService).getAccountByNumber(TEST_ACCOUNT_NUMBER);

        assertThrows(TransactionException.class, () -> transactionService.doDebit(createTransactionDto));
        verifyNoInteractions(transactionRepository, transactionMapper);
    }

    @Test
    void doCredit() {
        CreateTransactionDto createTransactionDto = getCreateTransactionDto(TransactionTypeEnum.CREDIT, 0);
        Transaction transaction = getTransaction();
        doReturn(transaction).when(transactionMapper).map(createTransactionDto);

        transactionService.doCredit(createTransactionDto);

        verify(transactionMapper).map(createTransactionDto);
        verify(transactionRepository).createTransaction(transaction);
    }

    private static Transaction getTransaction(){
        return Transaction.builder()
                .createdDate(LocalDateTime.now())
                .playerAccountNumber(TEST_ACCOUNT_NUMBER)
                .type(TransactionTypeEnum.CREDIT)
                .sum(100)
                .build();
    }

    private static PlayerAccount getPlayerAccount() {
        return PlayerAccount.builder()
                .accountNumber(TEST_ACCOUNT_NUMBER)
                .playerId(1)
                .balance(0L)
                .transactions(new ArrayList<>())
                .build();
    }

    private static CreateTransactionDto getCreateTransactionDto(TransactionTypeEnum type, int sum) {
        return CreateTransactionDto.builder()
                .playerAccountNumber(TEST_ACCOUNT_NUMBER)
                .createdDate(LocalDateTime.now())
                .type(type)
                .sum(sum)
                .build();
    }
}