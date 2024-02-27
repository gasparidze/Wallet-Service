package org.example.io.ylab.walletservicе.domain.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * dto объект сущности PlayerAudit
 */
@Value
@Builder
public class CreatePlayerAuditDto {
    private int playerId;
    private LocalDateTime lastLogin;
    private LocalDateTime lastLogOut;
    private LocalDateTime createdDate;
}
