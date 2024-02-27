package org.example.io.ylab.walletservicе.domain;

import lombok.*;
import java.time.LocalDateTime;

/**
 * Сущность - Лог игрока
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class PlayerAudit {
    /**
     * лог, генерируемый при вставке экземпляра класса в БД
     */
    private Integer id;
    /**
     * id игрока, с которым связан лог
     */
    private int playerId;
    /**
     * дата/время авторизации игрока в системе
     */
    private LocalDateTime lastLogin;
    /**
     * дата/время выхода игрока из системы
     */
    private LocalDateTime lastLogOut;
    /**
     * дата/время создания лога в системе
     */
    private LocalDateTime createdDate;
}
