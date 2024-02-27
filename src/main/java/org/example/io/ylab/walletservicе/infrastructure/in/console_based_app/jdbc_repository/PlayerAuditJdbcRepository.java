package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository;

import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.domain.PlayerAudit;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.ConnectionManager;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAuditRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Класс, отвечающий за прямое взаимодействие с таблицей player_audit в БД
 */
public class PlayerAuditJdbcRepository implements PlayerAuditRepositoryI {
    /**
     * класс, отвечающий за взаимодействие с таблицей Player в БД
     */
    private final PlayerRepositoryI playerRepository
            = PlayerRepositoryFactory.getPlayerRepository();

    /**
     * SQL запрос на вставку объекта PlayerAudit в БД
     */
    private static final String INSERT_LOG_SQL = """
                INSERT INTO wallet_service.player_audit (player_id, last_login, last_logout, created_date)
                VALUES (?, ?, ?, ?)
            """;

    /**
     * SQL запрос на получение лога игрока по его id
     */
    private static final String GET_LOG_BY_PLAYER_ID_SQL = """
            SELECT 
                id, 
                player_id,
                last_login,
                last_logout,
                created_date
            FROM wallet_service.player_audit
            WHERE player_id = ?
            """;

    /**
     * SQL запрос на обновление даты/времени авторизации игрока в системе по его id
     */
    private static final String UPDATE_LOG_LAST_LOGIN_BY_PLAYER_ID_SQL = """
            UPDATE wallet_service.player_audit
            SET last_login = ?
            WHERE player_id = ?
            """;

    /**
     * SQL запрос на обновление даты/времени выхода игрока из системы по его id
     */
    private static final String UPDATE_LOG_LAST_LOGOUT_BY_PLAYER_ID_SQL = """
            UPDATE wallet_service.player_audit
            SET last_logout = ?
            WHERE player_id = ?
            """;

    /**
     * метод, вставляющий объект PlayerAudit в БД
     * @param playerAudit - объект PlayerAudit
     * @return - объект PlayerAudit с сгенерированным id
     */
    @Override
    public PlayerAudit insertLog(PlayerAudit playerAudit) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOG_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, playerAudit.getPlayerId());
            preparedStatement.setObject(2, playerAudit.getLastLogin());
            preparedStatement.setObject(3, playerAudit.getLastLogOut());
            preparedStatement.setObject(4, playerAudit.getCreatedDate());

            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                playerAudit.setId(generatedKeys.getObject("id", Integer.class));
            }

            return playerAudit;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, обновляющий дату/время авторизации игрока в системе по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    @Override
    public void updateLogLastLogInById(int playerId) throws AuditException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_LOG_LAST_LOGIN_BY_PLAYER_ID_SQL)) {
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setObject(2, playerId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, обновляющий дату/время выхода игрока из системы по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    @Override
    public void updateLogLastLogOutById(int playerId) throws AuditException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_LOG_LAST_LOGOUT_BY_PLAYER_ID_SQL)) {
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setObject(2, playerId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, запрашивающий лог по id игрока
     * @param playerId - id игрока
     * @return - объект аудита игрока
     * @throws AuditException
     */
    @Override
    public Optional<PlayerAudit> findLogByPlayerId(int playerId) throws AuditException {
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_LOG_BY_PLAYER_ID_SQL)) {
            preparedStatement.setObject(1, playerId);

            var resultSet = preparedStatement.executeQuery();
            return resultSet.next()
                    ? Optional.of(buildEntity(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, который "строит" сущность PlayerAudit по информации из объекта ResultSet
     * @param resultSet - объект, в котором содержится информация, возвращаемая при SELECT запросе в базу
     * @return - объект PlayerAudit
     * @throws SQLException
     */
    private PlayerAudit buildEntity(ResultSet resultSet) throws SQLException {
        return PlayerAudit.builder()
                .id(resultSet.getObject("id", Integer.class))
                .playerId(playerRepository.findPlayerById(
                        resultSet.getObject("player_id", Integer.class),
                        resultSet.getStatement().getConnection()).orElse(null).getId())
                .lastLogin(resultSet.getObject("last_login", LocalDateTime.class))
                .lastLogOut(resultSet.getObject("last_logout", LocalDateTime.class))
                .createdDate(resultSet.getObject("created_date", LocalDateTime.class))
                .build();
    }
}
