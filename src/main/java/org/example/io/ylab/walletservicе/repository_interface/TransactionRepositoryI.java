package org.example.io.ylab.walletservicе.repository_interface;

import org.example.io.ylab.walletservicе.application_services.exceptions.TransactionException;
import org.example.io.ylab.walletservicе.domain.Transaction;

import java.util.List;
/**
 * интерфейс, отражающий поведение TransactionRepository
 */
public interface TransactionRepositoryI {
    /**
     * метод, вставляющий объект Transaction в БД
     * @param transaction - объект Transaction
     * @throws TransactionException
     */
    void createTransaction(Transaction transaction) throws TransactionException;

    /**
     * метод, запрашивающий список транзакций по номеру счета
     * @param accountNumber - номер счета
     * @return - список транзакций
     */
    List<Transaction> findTransactionsByAccountNumber(Long accountNumber);
}
