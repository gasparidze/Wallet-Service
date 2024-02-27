package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository.TransactionJdbcRepository;
import org.example.io.ylab.walletservicе.repository_interface.TransactionRepositoryI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса TransactionJdbcRepository
 */
@NoArgsConstructor(access = PRIVATE)
public class TransactionRepositoryFactory {
    private static TransactionRepositoryI transactionRepositoryInstance;

    public static TransactionRepositoryI getTransactionRepository() {
        if (transactionRepositoryInstance == null) {
            transactionRepositoryInstance = new TransactionJdbcRepository();
        }
        return transactionRepositoryInstance;
    }
}
