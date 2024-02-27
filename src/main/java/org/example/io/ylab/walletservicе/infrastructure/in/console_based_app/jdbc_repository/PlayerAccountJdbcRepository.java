package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository;

import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.ConnectionManager;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Класс, отвечающий за прямое взаимодействие с таблицей player_account в БД
 */
public class PlayerAccountJdbcRepository implements PlayerAccountRepositoryI {
    /**
     * класс, отвечающий за взаимодействие с таблицей Player в БД
     */
    private final PlayerRepositoryI playerRepository
            = PlayerRepositoryFactory.getPlayerRepository();

    /**
     * SQL запрос на вставку объекта PlayerAccount в БД
     */
    private static final String INSERT_PLAYER_ACCOUNT_SQL = """
                INSERT INTO wallet_service.player_account (player_id, balance) 
                VALUES (?, ?)
            """;

    /**
     * SQL запрос на получение счета игрока по его id
     */
    private static final String GET_ACCOUNT_BY_PLAYER_ID_SQL = """
        SELECT 
            account_number,
            player_id,
            balance
        FROM wallet_service.player_account
        WHERE player_id = ?
    """;

    /**
     * SQL запрос на обновление баланса счета по его номеру
     */
    private static final String UPDATE_BALANCE_BY_ACCOUNT_NUMBER_SQL = """
        UPDATE wallet_service.player_account
        SET balance = ?
        WHERE account_number = ?
    """;

    /**
     * SQL запрос на получение счета игрока по номеру счета
     */
    private static final String GET_ACCOUNT_BY_NUMBER_SQL = """
        SELECT 
            account_number,
            player_id,
            balance
        FROM wallet_service.player_account
        WHERE account_number = ?
    """;

    /**
     * метод, вставляющий объект PlayerAccount в БД
     * @param playerAccount - объект PlayerAccount
     * @return - объект PlayerAccount с сгенерированным номером счета (accountNumber)
     */
    @Override
    public PlayerAccount insertPlayerAccount(PlayerAccount playerAccount) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(INSERT_PLAYER_ACCOUNT_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, playerAccount.getPlayerId());
            preparedStatement.setObject(2, playerAccount.getBalance());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                playerAccount.setAccountNumber(generatedKeys.getObject("account_number", Long.class));
            }

            return playerAccount;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, запрашивающий счет игрока по его id из БД
     * @param playerId - id игрока
     * @return - баланс игрока
     */
    @Override
    public Optional<PlayerAccount> findAccountByPlayerId(int playerId){
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_PLAYER_ID_SQL)) {
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
     * метод, обновляющий баланс счета по номеру счета
     * @param accountNumber - счет игрока
     * @param balance - баланс счета
     */
    @Override
    public void updateBalanceByAccountNumber(Long accountNumber,Long balance){
        try (var connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(UPDATE_BALANCE_BY_ACCOUNT_NUMBER_SQL)) {
            preparedStatement.setObject(1, balance);
            preparedStatement.setObject(2, accountNumber);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, запрашивающий счет игрока по номеру счета
     * @param accountNumber - номер счета
     * @return - объект счета игрока
     */
    @Override
    public Optional<PlayerAccount> findAccountByNumber(Long accountNumber) {
        try (Connection connection = ConnectionManager.get();
             var preparedStatement = connection.prepareStatement(GET_ACCOUNT_BY_NUMBER_SQL)){
            preparedStatement.setObject(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next()
                    ? Optional.of(buildEntity(resultSet))
                    : Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, который "строит" сущность PlayerAccount по информации из объекта ResultSet
     * @param resultSet - объект, в котором содержится информация, возвращаемая при SELECT запросе в базу
     * @return - объект PlayerAccount
     * @throws SQLException
     */
    private PlayerAccount buildEntity(ResultSet resultSet) throws SQLException {
        return PlayerAccount.builder()
                .accountNumber(resultSet.getObject("account_number", Long.class))
                .playerId(playerRepository.findPlayerById(
                        resultSet.getObject("player_id", Integer.class),
                        resultSet.getStatement().getConnection()).orElse(null).getId())
                .balance(resultSet.getObject("balance", Long.class))
                .build();
    }
}
