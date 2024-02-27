package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.application_logic.TransactionService;
import org.example.io.ylab.walletservicе.application_services.TransactionServiceI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса TransactionService
 */
@NoArgsConstructor(access = PRIVATE)
public class TransactionServiceFactory {
    private static TransactionServiceI transactionServiceInstance;

    public static TransactionServiceI getTransactionService() {
        if (transactionServiceInstance == null) {
            transactionServiceInstance = new TransactionService(
                    TransactionRepositoryFactory.getTransactionRepository()
            );
        }
        return transactionServiceInstance;
    }
}
