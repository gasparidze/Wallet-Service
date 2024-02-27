package org.example.io.ylab.walletservicе.application_services;

import org.example.io.ylab.walletservicе.application_services.exceptions.TransactionException;
import org.example.io.ylab.walletservicе.domain.Transaction;
import org.example.io.ylab.walletservicе.domain.dto.CreateTransactionDto;

import java.util.List;

/**
 * интерфейс, отражающий поведение TransactionService
 */
public interface TransactionServiceI {
    /**
     * метод, создающий транзакцию на снятие денежных средств со счета
     * @param createTransactionDto - dto объект
     * @throws TransactionException
     */
    void doDebit(CreateTransactionDto createTransactionDto) throws TransactionException;

    /**
     * метод, создающий транзакцию на получение денежных средств со счета
     * @param createTransactionDto - dto объект
     */
    void doCredit(CreateTransactionDto createTransactionDto);

    /**
     * метод, получающий список транзакций по номеру счета
     * @param accountNumber - номер счета
     * @return - список транзакций
     */
    List<Transaction> getTransactionsByAccountNumber(Long accountNumber);
}
