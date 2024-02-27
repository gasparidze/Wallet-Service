package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository;

import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuthorizationException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.ConnectionManager;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;

import java.sql.*;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Класс, отвечающий за прямое взаимодействие с таблицей player в БД
 */
public class PlayerJdbcRepository implements PlayerRepositoryI {
    /**
     * SQL запрос на вставку объекта Player в БД
     */
    public static final String INSERT_PLAYER_SQL = """
                    INSERT INTO wallet_service.player (first_name, last_name, username, password)
                    VALUES (?, ?, ?, ?)
            """;

    /**
     * SQL запрос на получение игрока по его логину
     */
    private static final String GET_PLAYER_BY_USERNAME_SQL = """
                    SELECT 
                        id, 
                        first_name, 
                        last_name, 
                        username, 
                        password
                    FROM wallet_service.player
                    WHERE username = ?
            """;

    /**
     * SQL запрос на получение игрока по его id
     */
    private static final String GET_PLAYER_BY_Id_SQL = """
                    SELECT 
                        id, 
                        first_name, 
                        last_name, 
                        username, 
                        password
                    FROM wallet_service.player
                    WHERE id = ?
            """;

    /**
     * SQL запрос на получение игрока по его логину и паролю
     */
    private static final String GET_PLAYER_BY_USERNAME_AND_PASSWORD_SQL
            = GET_PLAYER_BY_USERNAME_SQL + " AND password = ?";

    /**
     * метод, вставляющий объект Player в БД
     * @param player - объект Player
     * @return - объект Player с сгенерированным id
     * @throws SQLException
     */
    @Override
    public Player savePlayer(Player player) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PLAYER_SQL, RETURN_GENERATED_KEYS)) {

            preparedStatement.setObject(1, player.getFirstName());
            preparedStatement.setObject(2, player.getLastName());
            preparedStatement.setObject(3, player.getUsername());
            preparedStatement.setObject(4, player.getPassword());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                player.setId(generatedKeys.getObject("id", Integer.class));
            }

            return player;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        checkMetadata();
    }
    private static void checkMetadata() throws SQLException {
        try (Connection connection = ConnectionManager.get()) {
            DatabaseMetaData metaData = connection.getMetaData();
            // catalog = dataBase
            ResultSet catalogs = metaData.getCatalogs();
            while (catalogs.next()){
                System.out.println(catalogs.getString("TABLE_CAT")); // postgres
                ResultSet schemas = metaData.getSchemas();
                while (schemas.next()){
                    //вернет все схемы для текущей БД
                    System.out.println(schemas.getString("TABLE_SCHEM"));
                    //вернет все таблицы для всех каталогов, т.к. передаем null
                    ResultSet tables = metaData.getTables(null, null, "%", null);
                    while (tables.next()){
                        System.out.println(tables.getString("TABLE_NAME"));
                    }
                }
            }
        }
    }

    /**
     * метод, запрашивающий игрока по его логину и паролю
     * @param username - логин
     * @param password - пароль
     * @return - boolean: авторизован ли игрок или нет
     */
    @Override
    public boolean findPlayerByCredentials(String username, String password) throws AuditException {
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_PLAYER_BY_USERNAME_AND_PASSWORD_SQL)) {
            preparedStatement.setFetchSize(50);
            preparedStatement.setQueryTimeout(100);
            preparedStatement.setMaxRows(100);

            preparedStatement.setObject(1, username);
            preparedStatement.setObject(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            throw new AuthorizationException(e.getMessage());
        }
    }

    /**
     * метод, запрашивающий игрока в системе по логину
     * @param username - логин
     * @return - найденный объект игрока
     */
    @Override
    public Optional<Player> findPlayerByUserName(String username) {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_PLAYER_BY_USERNAME_SQL)) {
            preparedStatement.setObject(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildEntity(resultSet))
                    : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, запрашивающий игрока в системе по id
     * @param playerId - id игрока
     * @param connection - текущее соединение с БД
     * @return - найденный объект игрока
     */
    public Optional<Player> findPlayerById(int playerId, Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(GET_PLAYER_BY_Id_SQL)) {
            preparedStatement.setObject(1, playerId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildEntity(resultSet))
                    : Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, который "строит" сущность Player по информации из объекта ResultSet
     * @param resultSet - объект, в котором содержится информация, возвращаемая при SELECT запросе в базу
     * @return - объект Player
     * @throws SQLException
     */
    private Player buildEntity(ResultSet resultSet) throws SQLException {
        return Player.builder()
                .id(resultSet.getObject("id", Integer.class))
                .firstName(resultSet.getObject("first_name", String.class))
                .lastName(resultSet.getObject("last_name", String.class))
                .username(resultSet.getObject("username", String.class))
                .password(resultSet.getObject("password", String.class))
                .build();
    }
}
