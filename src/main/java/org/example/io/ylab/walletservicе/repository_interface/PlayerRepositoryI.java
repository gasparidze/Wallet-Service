package org.example.io.ylab.walletservicе.repository_interface;

import org.example.io.ylab.walletservicе.domain.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
/**
 * интерфейс, отражающий поведение PlayerRepository
 */
public interface PlayerRepositoryI {
    /**
     * метод, вставляющий объект Player в БД
     * @param player - объект Player
     * @return - объект Player с сгенерированным id
     * @throws SQLException
     */
    Player savePlayer(Player player) throws SQLException;
    /**
     * метод, запрашивающий игрока по его логину и паролю
     * @param username - логин
     * @param password - пароль
     * @return - boolean: авторизован ли игрок или нет
     */
    boolean findPlayerByCredentials(String username, String password);

    /**
     * метод, запрашивающий игрока в системе по логину
     * @param username - логин
     * @return - найденный объект игрока
     */
    Optional<Player> findPlayerByUserName(String username);

    /**
     * метод, запрашивающий игрока в системе по id
     * @param playerId - id игрока
     * @param connection - текущее соединение с БД
     * @return - найденный объект игрока
     */
    Optional<Player> findPlayerById(int playerId, Connection connection);
}
