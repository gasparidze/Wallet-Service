package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util;

import org.example.io.ylab.walletservicе.domain.TransactionTypeEnum;
import org.example.io.ylab.walletservicе.domain.dto.CreateTransactionDto;

import java.time.LocalDateTime;

/**
 * Utility класс по созданию CreateTransactionDto
 */
public final class TransactionUtil {
    private TransactionUtil(){}

    /**
     * метод, создающий объект CreateTransactionDto
     * @param type - тип транзакции
     * @param accountNumber - номер счета
     * @param sum - сумма транзакции
     * @return - объект CreateTransactionDto
     */
    public static CreateTransactionDto buildTransactionEntity(TransactionTypeEnum type, Long accountNumber, int sum){
        return CreateTransactionDto.builder()
                .createdDate(LocalDateTime.now())
                .type(type)
                .playerAccountNumber(accountNumber)
                .sum(sum)
                .build();
    }
}