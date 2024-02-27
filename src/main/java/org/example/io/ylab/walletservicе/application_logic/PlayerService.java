package org.example.io.ylab.walletservicе.application_logic;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.io.ylab.walletservicе.application_services.PlayerServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuthorizationException;
import org.example.io.ylab.walletservicе.application_services.exceptions.RegistrationException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerDto;
import org.example.io.ylab.walletservicе.domain.mapper.PlayerMapper;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;

import java.sql.SQLException;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;
/**
 * Сервис для работы с игроком
 */
@RequiredArgsConstructor
public class PlayerService implements PlayerServiceI {
    /**
     * объект для обращения к таблице player
     */
    private final PlayerRepositoryI playerRepository;
    /**
     * mapper, сопоставляющий соответствующий dto объект с entity объекту
     */
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepositoryI playerRepository) {
        this.playerRepository = playerRepository;
        playerMapper = PlayerMapper.getInstance();
    }

    /**
     * метод, создающий объект Player
     * @param createPlayerDto - dto объект
     * @return - объект Player со вставленном id
     * @throws RegistrationException
     */
    @Override
    public Player registerNewPlayer(CreatePlayerDto createPlayerDto) throws RegistrationException {
        if (playerRepository.findPlayerByUserName(createPlayerDto.getUsername()).isPresent()) {
            throw new RegistrationException("Игрок с таким username уже существует!");
        }
        Player registeredPlayer = playerMapper.map(createPlayerDto);

        try {
            registeredPlayer = playerRepository.savePlayer(registeredPlayer);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return registeredPlayer;
     }

    /**
     * метод, авторизующий игрока в системе по логину и паролю
     * @param username - логин
     * @param password - пароль
     * @return - результат: авторизован ли игрок или нет
     * @throws AuthorizationException
     * @throws AuditException
     */
    @Override
    public boolean authorizePlayer(String username, String password) throws AuthorizationException, AuditException {
        if (!playerRepository.findPlayerByCredentials(username, password)) {
            throw new AuthorizationException("Неверный логин и/или пароль");
        }

        return true;
    }

    /**
     * метод, получающий игрока по его логину
     * @param username - логин
     * @return - найденный объект игрока
     */
    @Override
    public Optional<Player> getPlayerByUserName(String username) {
        return playerRepository.findPlayerByUserName(username);
    }
}
