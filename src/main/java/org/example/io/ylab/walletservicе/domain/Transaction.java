package org.example.io.ylab.walletservicе.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность - Транзакция
 */
@Getter
@Setter
@ToString
@Builder
public class Transaction {
    /**
     * id, генерируемый при вставке экземпляра класса в БД
     */
    private UUID id;
    /**
     * дата/время создания транзакции в системе
     */
    private LocalDateTime createdDate;
    /**
     * тип транзакции
     */
    private TransactionTypeEnum type;
    /**
     * номер счета, с которым связана транзакция
     */
    private Long playerAccountNumber;
    /**
     * сумма транзакции
     */
    private int sum;
}
