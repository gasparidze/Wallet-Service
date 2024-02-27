package org.example.io.ylab.walletservicе.domain;

import lombok.*;
import java.util.List;

/**
 * Сущность - Счет игрока
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class PlayerAccount {
    /**
     * номер счета, генерируемый при вставке экземпляра класса в БД
     */
    private long accountNumber;
    /**
     * id игрока, с которым связан счет
     */
    private int playerId;
    /**
     * баланс счета
     */
    private Long balance;
    /**
     * список транзакций, связанных со счетом
     */
    private List<Transaction> transactions;
}