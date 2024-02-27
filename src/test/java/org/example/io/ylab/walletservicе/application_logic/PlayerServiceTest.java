package org.example.io.ylab.walletservicе.application_logic;

import org.example.io.ylab.walletservicе.application_services.exceptions.AuthorizationException;
import org.example.io.ylab.walletservicе.application_services.exceptions.RegistrationException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerDto;
import org.example.io.ylab.walletservicе.domain.mapper.PlayerMapper;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
    private Player player;
    @Mock
    private PlayerRepositoryI playerRepository;
    @Mock
    private PlayerMapper playerMapper;
    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void initData(){
        player = getPlayer();
    }

    @Test
    void registerNewPlayer() throws SQLException {
        CreatePlayerDto createPlayerDto = getCreatePlayerDto();
        doReturn(Optional.empty()).when(playerRepository).findPlayerByUserName(createPlayerDto.getUsername());
        doReturn(player).when(playerMapper).map(createPlayerDto);
        doReturn(player).when(playerRepository).savePlayer(player);

        Player actualResult = playerService.registerNewPlayer(createPlayerDto);

        assertThat(player).isEqualTo(actualResult);
        verify(playerRepository).findPlayerByUserName(createPlayerDto.getUsername());
        verify(playerMapper).map(createPlayerDto);
        verify(playerRepository).savePlayer(player);
    }

    @Test
    void registerNewPlayerExceptionScenario() {
        CreatePlayerDto createPlayerDto = getCreatePlayerDto();
        doReturn(Optional.of(createPlayerDto)).when(playerRepository).findPlayerByUserName(createPlayerDto.getUsername());

        assertThrows(RegistrationException.class, () -> playerService.registerNewPlayer(createPlayerDto));
        verifyNoInteractions(playerMapper);
    }

    @Test
    void authorizePlayer(){
        doReturn(true).when(playerRepository)
                .findPlayerByCredentials(player.getUsername(), player.getPassword());

        boolean actualResult = playerService.authorizePlayer(player.getUsername(), player.getPassword());

        assertThat(true).isEqualTo(actualResult);
        verify(playerRepository).findPlayerByCredentials(player.getUsername(), player.getPassword());
    }

    @Test
    void authorizePlayerExceptionScenario(){
        doReturn(false).when(playerRepository)
                .findPlayerByCredentials(player.getUsername(), player.getPassword());

        assertThrows(AuthorizationException.class,
                () -> playerService.authorizePlayer(player.getUsername(), player.getPassword()));
    }

    @Test
    void getPlayerByUserName(){
        doReturn(Optional.of(player)).when(playerRepository).findPlayerByUserName(player.getUsername());

        Optional<Player> actualResult = playerService.getPlayerByUserName(player.getUsername());

        assertThat(Optional.of(player)).isEqualTo(actualResult);
        verify(playerRepository).findPlayerByUserName(player.getUsername());
    }

    private static CreatePlayerDto getCreatePlayerDto() {
        return CreatePlayerDto.builder()
                .username("test")
                .password("123")
                .firstName("Test")
                .lastName("Testov")
                .build();
    }


    private static Player getPlayer() {
        return Player.builder()
                .username("test")
                .password("123")
                .firstName("Test")
                .lastName("Testov")
                .build();
    }
}