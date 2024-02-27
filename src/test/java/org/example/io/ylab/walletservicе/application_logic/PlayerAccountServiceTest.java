package org.example.io.ylab.walletservicе.application_logic;

import org.example.io.ylab.walletservicе.application_services.exceptions.PlayerAccountException;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAccountDto;
import org.example.io.ylab.walletservicе.domain.mapper.PlayerAccountMapper;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerAccountServiceTest {
    private static final Long TEST_ACCOUNT_NUMBER = 123456789L;
    private static final Integer TEST_PLAYER_ID = 1;
    @Mock
    private PlayerAccountRepositoryI playerAccountRepository;
    @Mock
    private PlayerAccountMapper playerAccountMapper;
    @InjectMocks
    private PlayerAccountService playerAccountService;

    @Test
    void createPlayerAccount() {
        CreatePlayerAccountDto createPlayerAccountDto = getCreatePlayerAccountDto();
        PlayerAccount playerAccount = getPlayerAccount();
        doReturn(playerAccount).when(playerAccountMapper).map(createPlayerAccountDto);

        playerAccountService.createPlayerAccount(createPlayerAccountDto);

        verify(playerAccountMapper).map(createPlayerAccountDto);
        verify(playerAccountRepository).insertPlayerAccount(playerAccount);
    }

    @Test
    void getBalanceByPlayerId() {
        PlayerAccount playerAccount = getPlayerAccount();
        doReturn(Optional.of(playerAccount)).when(playerAccountRepository).findAccountByPlayerId(1);

        Long actualResult = playerAccountService.getBalanceByPlayerId(TEST_PLAYER_ID);

        assertThat(playerAccount.getBalance()).isEqualTo(actualResult);
        verify(playerAccountRepository).findAccountByPlayerId(TEST_PLAYER_ID);
    }

    @Test
    void getAccountByPlayerId() {
        PlayerAccount playerAccount = getPlayerAccount();
        doReturn(Optional.of(playerAccount)).when(playerAccountRepository).findAccountByPlayerId(TEST_PLAYER_ID);

        Optional<PlayerAccount> actualResult = playerAccountService.getAccountByPlayerId(TEST_PLAYER_ID);

        assertThat(Optional.of(playerAccount)).isEqualTo(actualResult);
        verify(playerAccountRepository).findAccountByPlayerId(TEST_PLAYER_ID);
    }

    @Test
    void getAccountByPlayerIdExceptionScenario() {
        doReturn(Optional.empty()).when(playerAccountRepository).findAccountByPlayerId(TEST_PLAYER_ID);

        assertThrows(PlayerAccountException.class, () -> playerAccountService.getAccountByPlayerId(TEST_PLAYER_ID));
        verify(playerAccountRepository).findAccountByPlayerId(1);
    }

    @Test
    void setBalanceByPlayerAccountNumber() {
        playerAccountService.setBalanceByPlayerAccountNumber(TEST_ACCOUNT_NUMBER, 153L);

        verify(playerAccountRepository).updateBalanceByAccountNumber(TEST_ACCOUNT_NUMBER, 153L);
    }

    @Test
    void getAccountByNumber(){
        PlayerAccount playerAccount = getPlayerAccount();
        doReturn(Optional.of(playerAccount)).when(playerAccountRepository).findAccountByNumber(TEST_ACCOUNT_NUMBER);

        Optional<PlayerAccount> actualResult = playerAccountService.getAccountByNumber(TEST_ACCOUNT_NUMBER);

        assertThat(Optional.of(playerAccount)).isEqualTo(actualResult);
        verify(playerAccountRepository).findAccountByNumber(TEST_ACCOUNT_NUMBER);
    }

    private static PlayerAccount getPlayerAccount() {
        return PlayerAccount.builder()
                .playerId(TEST_PLAYER_ID)
                .balance(0L)
                .transactions(new ArrayList<>())
                .build();
    }

    private static CreatePlayerAccountDto getCreatePlayerAccountDto() {
        return CreatePlayerAccountDto.builder()
                .playerId(TEST_PLAYER_ID)
                .transactions(new ArrayList<>())
                .balance(0L)
                .build();
    }
}