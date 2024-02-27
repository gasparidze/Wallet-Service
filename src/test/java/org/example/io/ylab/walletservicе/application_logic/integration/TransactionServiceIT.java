package org.example.io.ylab.walletservicе.application_logic.integration;

import lombok.SneakyThrows;
import org.example.io.ylab.walletservicе.application_services.TransactionServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.TransactionException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.Transaction;
import org.example.io.ylab.walletservicе.domain.TransactionTypeEnum;
import org.example.io.ylab.walletservicе.domain.dto.CreateTransactionDto;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAccountRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.TransactionRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.TransactionServiceFactory;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.TransactionRepositoryI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionServiceIT extends IntegrationTestBase{
    private Long TEST_PLAYER_ACCOUNT_NUMBER;
    private static TransactionRepositoryI transactionRepository;
    private static TransactionServiceI transactionService;
    private static PlayerAccountRepositoryI playerAccountRepository;
    private static PlayerRepositoryI playerRepository;

    @BeforeAll
    static void init(){
        transactionRepository = TransactionRepositoryFactory.getTransactionRepository();
        transactionService = TransactionServiceFactory.getTransactionService();
        playerAccountRepository = PlayerAccountRepositoryFactory.getAccountPlayerRepository();
        playerRepository = PlayerRepositoryFactory.getPlayerRepository();
    }

    @BeforeEach
    @SneakyThrows
    void prepareData(){
        int playerId = playerRepository.savePlayer(getPlayer()).getId();
        playerAccountRepository.insertPlayerAccount(getPlayerAccount(playerId));
        TEST_PLAYER_ACCOUNT_NUMBER = PlayerAccountRepositoryFactory
                .getAccountPlayerRepository()
                .findAccountByPlayerId(playerId)
                .get().getAccountNumber();
    }

    @Test
    void doDebit(){
        CreateTransactionDto createTransactionDto
                = getCreateTransactionDto(TransactionTypeEnum.DEBIT, TEST_PLAYER_ACCOUNT_NUMBER, 50);

        transactionService.doDebit(createTransactionDto);
        List<Transaction> transactionsByAccountNumber
                = transactionRepository.findTransactionsByAccountNumber(TEST_PLAYER_ACCOUNT_NUMBER);

        assertThat(transactionsByAccountNumber.size()).isEqualTo(1);
    }

    @Test
    void doDebitExceptionScenario(){
        CreateTransactionDto createTransactionDto
                = getCreateTransactionDto(TransactionTypeEnum.DEBIT, TEST_PLAYER_ACCOUNT_NUMBER, 200);

        TransactionException transactionException = assertThrows(TransactionException.class,
                () -> transactionService.doDebit(createTransactionDto));
        List<Transaction> transactionsByAccountNumber
                = transactionRepository.findTransactionsByAccountNumber(TEST_PLAYER_ACCOUNT_NUMBER);

        assertThat(transactionsByAccountNumber.size()).isEqualTo(0);
        assertThat("На счету недостаточно средств!").isEqualTo(transactionException.getMessage());
    }

    @Test
    void doCredit(){
        CreateTransactionDto createTransactionDto
                = getCreateTransactionDto(TransactionTypeEnum.CREDIT, TEST_PLAYER_ACCOUNT_NUMBER, 100);

        transactionService.doCredit(createTransactionDto);
        List<Transaction> transactionsByAccountNumber
                = transactionRepository.findTransactionsByAccountNumber(TEST_PLAYER_ACCOUNT_NUMBER);

        assertThat(transactionsByAccountNumber.size()).isEqualTo(1);
    }

    private Player getPlayer() {
        return Player.builder()
                .firstName("Test")
                .lastName("Testov")
                .username("test@mail.ru")
                .password("123")
                .build();
    }

    private PlayerAccount getPlayerAccount(int playerId) {
        return PlayerAccount.builder()
                .playerId(playerId)
                .balance(100L)
                .transactions(new ArrayList<>())
                .build();
    }

    private CreateTransactionDto getCreateTransactionDto(TransactionTypeEnum type, Long accountNumber, int sum) {
        return CreateTransactionDto.builder()
                .playerAccountNumber(accountNumber)
                .createdDate(LocalDateTime.now())
                .type(type)
                .sum(sum)
                .build();
    }
}