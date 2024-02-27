package org.example.io.ylab.walletservicе.domain.dto;

import lombok.Builder;
import lombok.Value;
import org.example.io.ylab.walletservicе.domain.Transaction;

import java.util.List;

/**
 * dto объект сущности PlayerAccount
 */
@Value
@Builder
public class CreatePlayerAccountDto {
    private int playerId;
    private Long balance;
    private List<Transaction> transactions;
}
