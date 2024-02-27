package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository;

import org.example.io.ylab.walletservicе.application_services.exceptions.TransactionException;
import org.example.io.ylab.walletservicе.domain.Transaction;
import org.example.io.ylab.walletservicе.domain.TransactionTypeEnum;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.ConnectionManager;
import org.example.io.ylab.walletservicе.repository_interface.TransactionRepositoryI;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * Класс, отвечающий за прямое взаимодействие с таблицей transaction в БД
 */
public class TransactionJdbcRepository implements TransactionRepositoryI {
    /**
     * SQL запрос на вставку объекта Transaction в БД
     */
    private static final String INSERT_TRANSACTION_SQL = """
        INSERT INTO wallet_service.transaction (created_date, type, player_account_number, sum)
        VALUES (?, ?, ?, ?)
    """;

    /**
     * SQL запрос на получение транзакций, связанных к определенным номером счета
     */
    private static final String GET_TRANSACTIONS_BY_ACCOUNT_NUMBER_SQL = """
        SELECT
            id,
            created_date,
            type,
            player_account_number,
            sum
        FROM wallet_service.transaction
        WHERE player_account_number = ?
    """;

    /**
     * метод, вставляющий объект Transaction в БД
     * @param transaction - объект Transaction
     * @throws TransactionException
     */
    @Override
    public void createTransaction(Transaction transaction){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_TRANSACTION_SQL, RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1, transaction.getCreatedDate());
            preparedStatement.setObject(2, String.valueOf(transaction.getType()));
            preparedStatement.setObject(3, transaction.getPlayerAccountNumber());
            preparedStatement.setObject(4, transaction.getSum());

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getObject("id", UUID.class));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * метод, запрашивающий список транзакций по номеру счета
     * @param accountNumber - номер счета
     * @return - список транзакций
     */
    @Override
    public List<Transaction> findTransactionsByAccountNumber(Long accountNumber){
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_TRANSACTIONS_BY_ACCOUNT_NUMBER_SQL)) {

            preparedStatement.setObject(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while (resultSet.next()){
                transactions.add(Transaction.builder()
                        .id(resultSet.getObject("id", UUID.class))
                        .createdDate(resultSet.getObject("created_date", LocalDateTime.class))
                        .type(TransactionTypeEnum.valueOf(resultSet.getString("type")))
                        .playerAccountNumber(accountNumber)
                        .sum(resultSet.getObject("sum", Integer.class))
                        .build());
            }

            return transactions;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
