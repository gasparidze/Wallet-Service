package org.example.io.ylab.walletservicе.domain.dto;

import lombok.Builder;
import lombok.Value;
import org.example.io.ylab.walletservicе.domain.TransactionTypeEnum;

import java.time.LocalDateTime;

/**
 * dto объект сущности Transaction
 */
@Value
@Builder
public class CreateTransactionDto {
    private LocalDateTime createdDate;
    private TransactionTypeEnum type;
    private Long playerAccountNumber;
    private int sum;
}
