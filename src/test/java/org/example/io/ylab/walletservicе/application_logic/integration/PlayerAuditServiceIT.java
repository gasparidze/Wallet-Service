package org.example.io.ylab.walletservicе.application_logic.integration;

import lombok.SneakyThrows;
import org.example.io.ylab.walletservicе.application_services.PlayerAuditServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.PlayerAudit;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAuditDto;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAuditRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAuditServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerRepositoryFactory;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAuditRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerAuditServiceIT extends IntegrationTestBase{
    private Integer TEST_PLAYER_ID;
    private static PlayerAuditRepositoryI playerAuditRepository;
    private static PlayerRepositoryI playerRepository;
    private static PlayerAuditServiceI playerAuditService;

    @BeforeAll
    static void init(){
        playerAuditRepository = PlayerAuditRepositoryFactory.getPlayerAuditRepository();
        playerRepository = PlayerRepositoryFactory.getPlayerRepository();
        playerAuditService = PlayerAuditServiceFactory.getPlayerAuditService();
    }

    @BeforeEach
    @SneakyThrows
    void prepareData(){
        TEST_PLAYER_ID = playerRepository.savePlayer(getPlayer()).getId();
        playerAuditRepository.insertLog(getPlayerAudit());
    }

    @Test
    void createLog(){
        CreatePlayerAuditDto createPlayerAuditDto = getCreatePlayerAuditDto(TEST_PLAYER_ID);

        PlayerAudit actualResult = playerAuditService.createLog(createPlayerAuditDto);

        assertNotNull(actualResult.getId());
    }

    @Test
    void setLastLogInLog(){
        LocalDateTime lastLogin = playerAuditRepository.findLogByPlayerId(TEST_PLAYER_ID).get().getLastLogin();

        playerAuditService.setLastLogInLog(TEST_PLAYER_ID);
        LocalDateTime updatedLastLogin = playerAuditRepository.findLogByPlayerId(TEST_PLAYER_ID).get().getLastLogin();

        assertNotEquals(lastLogin, updatedLastLogin);
    }

    @Test
    void setLastLogOutLog(){
        LocalDateTime lastLogout = playerAuditRepository.findLogByPlayerId(TEST_PLAYER_ID).get().getLastLogOut();

        playerAuditService.setLastLogInLog(TEST_PLAYER_ID);
        LocalDateTime updatedLastLogout = playerAuditRepository.findLogByPlayerId(TEST_PLAYER_ID).get().getLastLogin();

        assertNotEquals(lastLogout, updatedLastLogout);
    }

    @Test
    void getLogById(){
        Optional<PlayerAudit> actualResult = playerAuditService.getLogByPlayerId(TEST_PLAYER_ID);

        assertThat(actualResult).isPresent();
    }

    @Test
    void getLogByIdExceptionScenario(){
        AuditException auditException = assertThrows(AuditException.class, () -> playerAuditService.getLogByPlayerId(0));

        assertThat("У данного пользователя отсутствуют логи").isEqualTo(auditException.getMessage());
    }

    private PlayerAudit getPlayerAudit() {
        return PlayerAudit
                .builder()
                .playerId(TEST_PLAYER_ID)
                .lastLogin(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
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

    private CreatePlayerAuditDto getCreatePlayerAuditDto(int playerId) {
        return CreatePlayerAuditDto.builder()
                .playerId(playerId)
                .lastLogin(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
