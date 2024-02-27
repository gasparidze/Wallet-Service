package org.example.io.ylab.walletservicе.application_services;

import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuthorizationException;
import org.example.io.ylab.walletservicе.application_services.exceptions.RegistrationException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerDto;

import java.sql.SQLException;
import java.util.Optional;

/**
 * интерфейс, отражающий поведение PlayerService
 */
public interface PlayerServiceI {
    /**
     * метод, создающий объект Player
     * @param createPlayerDto - dto объект
     * @return - объект Player со вставленном id
     * @throws RegistrationException
     */
    Player registerNewPlayer(CreatePlayerDto createPlayerDto) throws RegistrationException, SQLException;

    /**
     * метод, авторизующий игрока в системе по логину и паролю
     * @param username - логин
     * @param password - пароль
     * @return - boolean: авторизован ли игрок или нет
     * @throws AuthorizationException
     * @throws AuditException
     */
    boolean authorizePlayer(String username, String password) throws AuthorizationException, AuditException;

    /**
     * метод, получающий игрока по его логину
     * @param username - логин
     * @return - найденный объект игрока
     */
    Optional<Player> getPlayerByUserName(String username);
}
