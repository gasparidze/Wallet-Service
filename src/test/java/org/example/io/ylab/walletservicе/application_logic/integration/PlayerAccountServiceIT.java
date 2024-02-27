package org.example.io.ylab.walletservicе.application_logic.integration;

import lombok.SneakyThrows;
import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.PlayerAccountException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAccountDto;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAccountRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAccountServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerRepositoryFactory;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerAccountServiceIT extends IntegrationTestBase{
    private Integer TEST_PLAYER_ID;
    private static PlayerAccountRepositoryI playerAccountRepository;
    private static PlayerRepositoryI playerRepository;
    private static PlayerAccountServiceI playerAccountService;

    @BeforeAll
    static void init(){
        playerAccountRepository = PlayerAccountRepositoryFactory.getAccountPlayerRepository();
        playerRepository = PlayerRepositoryFactory.getPlayerRepository();
        playerAccountService =  PlayerAccountServiceFactory.getPlayerService();
    }

    @BeforeEach
    @SneakyThrows
    void prepareData(){
        TEST_PLAYER_ID = playerRepository.savePlayer(getPlayer()).getId();
        playerAccountRepository.insertPlayerAccount(getPlayerAccount());
    }

    @Test
    void createPlayerAccount(){
        CreatePlayerAccountDto createPlayerAccountDto = getCreatePlayerAccountDto(TEST_PLAYER_ID);

        PlayerAccount actualResult = playerAccountService.createPlayerAccount(createPlayerAccountDto);

        assertNotNull(actualResult.getAccountNumber());
    }

    @Test
    void getBalanceByPlayerId(){
        Long actualBalance = playerAccountService.getBalanceByPlayerId(TEST_PLAYER_ID);

        assertThat(100L).isEqualTo(actualBalance);
    }

    @Test
    void getAccountByPlayerId(){
        Optional<PlayerAccount> actualPlayerAccount = playerAccountService.getAccountByPlayerId(TEST_PLAYER_ID);

        assertThat(actualPlayerAccount).isPresent();
    }

    @Test
    void getAccountByPlayerIdExceptionScenario(){
        PlayerAccountException playerAccountException = assertThrows(PlayerAccountException.class,
                () -> playerAccountService.getAccountByPlayerId(0));

        assertThat("У данного клиента нет счета").isEqualTo(playerAccountException.getMessage());
    }

    @Test
    void setBalanceByPlayerAccountNumber(){
        PlayerAccount foundPlayerAccount = playerAccountRepository.findAccountByPlayerId(TEST_PLAYER_ID).get();
        Long expectedBalance = foundPlayerAccount.getBalance() + 100L;

        playerAccountService.setBalanceByPlayerAccountNumber(
                foundPlayerAccount.getAccountNumber(),
                expectedBalance
        );
        Long actualBalance = playerAccountRepository.findAccountByPlayerId(TEST_PLAYER_ID).get().getBalance();

        assertThat(expectedBalance).isEqualTo(actualBalance);
    }

    @Test
    void getAccountByNumber(){
        PlayerAccount foundPlayerAccount = playerAccountRepository.findAccountByPlayerId(TEST_PLAYER_ID).get();

        Optional<PlayerAccount> actualPlayerAccount
                = playerAccountService.getAccountByNumber(foundPlayerAccount.getAccountNumber());

        assertThat(actualPlayerAccount).isPresent();
    }

    private PlayerAccount getPlayerAccount() {
        return PlayerAccount.builder()
                .playerId(TEST_PLAYER_ID)
                .balance(100L)
                .transactions(new ArrayList<>())
                .build();
    }

    private Player getPlayer() {
        return Player.builder()
                .firstName("test")
                .lastName("testov")
                .username("test@mail.ru")
                .password("123")
                .build();
    }

    private CreatePlayerAccountDto getCreatePlayerAccountDto(int playerId) {
        return CreatePlayerAccountDto.builder()
                .playerId(playerId)
                .transactions(new ArrayList<>())
                .balance(100L)
                .build();
    }
}
