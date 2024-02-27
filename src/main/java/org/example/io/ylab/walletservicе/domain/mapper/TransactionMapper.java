package org.example.io.ylab.walletservicе.domain.mapper;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.domain.Transaction;
import org.example.io.ylab.walletservicе.domain.dto.CreateTransactionDto;

import static lombok.AccessLevel.PRIVATE;

/**
 * Класс-маппер для Transaction
 */
@NoArgsConstructor(access = PRIVATE)
public class TransactionMapper implements Mapper<CreateTransactionDto, Transaction>{
    /**
     * простейщая реализация singleton класса-маппера
     */
    private static final TransactionMapper INSTANCE = new TransactionMapper();
    public static TransactionMapper getInstance() {
        return INSTANCE;
    }

    /**
     * метод, сопоставляющий dto объект CreateTransactionDto к соответствующей сущности Transaction
     * @param object - dto объект
     * @return - соответствующая сущность
     */
    @Override
    public Transaction map(CreateTransactionDto object) {
        return Transaction.builder()
                .playerAccountNumber(object.getPlayerAccountNumber())
                .type(object.getType())
                .sum(object.getSum())
                .createdDate(object.getCreatedDate())
                .build();
    }
}
