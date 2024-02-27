package org.example.io.ylab.walletservicе.application_logic;

import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.domain.PlayerAudit;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAuditDto;
import org.example.io.ylab.walletservicе.domain.mapper.PlayerAuditMapper;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAuditRepositoryI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerAuditServiceTest {
    private static final Integer TEST_PLAYER_ID = 1;
    @Mock
    private PlayerAuditRepositoryI playerAuditRepository;
    @Mock
    private PlayerAuditMapper playerAuditMapper;
    @InjectMocks
    private PlayerAuditService playerAuditService;

    @Test
    void createLog(){
        CreatePlayerAuditDto createPlayerAuditDto = getCreatePlayerAuditDto();
        PlayerAudit playerAudit = getPlayerAudit();
        doReturn(playerAudit).when(playerAuditMapper).map(createPlayerAuditDto);

        playerAuditService.createLog(createPlayerAuditDto);

        verify(playerAuditMapper).map(createPlayerAuditDto);
        verify(playerAuditRepository).insertLog(playerAudit);
    }

    @Test
    void setLastLogInLog() {
        playerAuditService.setLastLogInLog(anyInt());

        verify(playerAuditRepository).updateLogLastLogInById(anyInt());
    }

    @Test
    void setLastLogOutLog() {
        playerAuditService.setLastLogOutLog(anyInt());

        verify(playerAuditRepository).updateLogLastLogOutById(anyInt());
    }

    @Test
    void getLogById() {
        PlayerAudit playerAudit = getPlayerAudit();
        doReturn(Optional.of(playerAudit)).when(playerAuditRepository).findLogByPlayerId(TEST_PLAYER_ID);

        Optional<PlayerAudit> actualResult = playerAuditService.getLogByPlayerId(TEST_PLAYER_ID);

        assertThat(Optional.of(playerAudit)).isEqualTo(actualResult);
        verify(playerAuditRepository).findLogByPlayerId(TEST_PLAYER_ID);
    }

    @Test
    void getLogByIdExceptionScenario(){
        doReturn(Optional.empty()).when(playerAuditRepository).findLogByPlayerId(TEST_PLAYER_ID);

        assertThrows(AuditException.class, () -> playerAuditService.getLogByPlayerId(TEST_PLAYER_ID));
    }

    private static CreatePlayerAuditDto getCreatePlayerAuditDto() {
        return CreatePlayerAuditDto.builder()
                .playerId(TEST_PLAYER_ID)
                .lastLogin(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();
    }

    private static PlayerAudit getPlayerAudit() {
        return PlayerAudit.builder()
                .playerId(TEST_PLAYER_ID)
                .lastLogin(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();
    }
}