package org.example.io.ylab.walletservicе.domain.mapper;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAccountDto;

import static lombok.AccessLevel.PRIVATE;

/**
 * Класс-маппер для PlayerAccount
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerAccountMapper implements Mapper<CreatePlayerAccountDto, PlayerAccount>{
    /**
     * простейщая реализация singleton класса-маппера
     */
    private static final PlayerAccountMapper INSTANCE = new PlayerAccountMapper();
    public static PlayerAccountMapper getInstance() {
        return INSTANCE;
    }

    /**
     * метод, сопоставляющий dto объект CreatePlayerAccountDto к соответствующей сущности PlayerAccount
     * @param object - dto объект
     * @return - соответствующая сущность
     */
    @Override
    public PlayerAccount map(CreatePlayerAccountDto object) {
        return PlayerAccount.builder()
                .playerId(object.getPlayerId())
                .balance(object.getBalance())
                .transactions(object.getTransactions())
                .build();
    }
}
