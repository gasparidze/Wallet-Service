package org.example.io.ylab.walletservicе.domain.mapper;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.domain.PlayerAudit;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAuditDto;

import static lombok.AccessLevel.PRIVATE;

/**
 * Класс-маппер для PlayerAudit
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerAuditMapper implements Mapper<CreatePlayerAuditDto, PlayerAudit>{
    /**
     * простейщая реализация singleton класса-маппера
     */
    private static final PlayerAuditMapper INSTANCE = new PlayerAuditMapper();
    public static PlayerAuditMapper getInstance() {
        return INSTANCE;
    }

    /**
     * метод, сопоставляющий dto объект CreatePlayerAuditDto к соответствующей сущности PlayerAudit
     * @param object - dto объект
     * @return - соответствующая сущность
     */
    @Override
    public PlayerAudit map(CreatePlayerAuditDto object) {
        return PlayerAudit.builder()
                .playerId(object.getPlayerId())
                .lastLogin(object.getLastLogin())
                .createdDate(object.getCreatedDate())
                .build();
    }
}
