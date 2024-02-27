package org.example.io.ylab.walletservicе.application_logic.integration;

import lombok.SneakyThrows;
import org.example.io.ylab.walletservicе.application_services.PlayerServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuthorizationException;
import org.example.io.ylab.walletservicе.application_services.exceptions.RegistrationException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerDto;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerServiceFactory;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceIT extends IntegrationTestBase{
    private static final String TEST_FIRSTNAME = "Test";
    private static final String TEST_LASTNAME = "Testov";
    private static final String TEST_USERNAME = "test@mail.ru";
    private static final String TEST_PASSWORD = "123";
    private static PlayerRepositoryI playerRepository;
    private static PlayerServiceI playerService;

    @BeforeAll
    static void init(){
        playerRepository = PlayerRepositoryFactory.getPlayerRepository();
        playerService = PlayerServiceFactory.getPlayerService();
    }

    @BeforeEach
    @SneakyThrows
    void prepareData(){
        playerRepository.savePlayer(getPlayer());
    }

    @Test
    @SneakyThrows
    void registerNewPlayer(){
        CreatePlayerDto createPlayerDto = getCreatePlayerDto("test1@mail.ru");

        Player actualResult = playerService.registerNewPlayer(createPlayerDto);

        assertNotNull(actualResult.getId());
    }

    @Test
    @SneakyThrows
    void registerNewPlayerExceptionScenario(){
        CreatePlayerDto createPlayerDto = getCreatePlayerDto(TEST_USERNAME);

        RegistrationException exception = assertThrows(RegistrationException.class,
                () -> playerService.registerNewPlayer(createPlayerDto));
        assertThat("Игрок с таким username уже существует!").isEqualTo(exception.getMessage());
    }

    @Test
    @SneakyThrows
    void authorizePlayer(){
        boolean isAuthorized = playerService.authorizePlayer(TEST_USERNAME, TEST_PASSWORD);

        assertTrue(isAuthorized);
    }

    @Test
    void authorizePlayerExceptionScenario(){
        AuthorizationException authorizationException = assertThrows(AuthorizationException.class,
                () -> playerService.authorizePlayer("dummy", "dummy"));

        assertThat("Неверный логин и/или пароль").isEqualTo(authorizationException.getMessage());
    }

    @Test
    @SneakyThrows
    void getPlayerByUserName(){
        Optional<Player> actualResult = playerService.getPlayerByUserName(TEST_USERNAME);

        assertThat(actualResult).isPresent();

    }

    private CreatePlayerDto getCreatePlayerDto(String username) {
        return CreatePlayerDto.builder()
                .username(username)
                .password(TEST_PASSWORD)
                .firstName("Test")
                .lastName("Testov")
                .build();
    }

    private Player getPlayer() {
        return Player.builder()
                .firstName(TEST_FIRSTNAME)
                .lastName(TEST_LASTNAME)
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .build();
    }
}
