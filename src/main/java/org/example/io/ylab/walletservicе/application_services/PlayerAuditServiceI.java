package org.example.io.ylab.walletservicе.application_services;

import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.domain.PlayerAudit;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAuditDto;

import java.util.Optional;

/**
 * интерфейс, отражающий поведение PlayerAuditService
 */
public interface PlayerAuditServiceI {

    /**
     * метод, создающий объект PlayerAudit
     * @param createPlayerAuditDto - dto объект
     * @return - объект PlayerAudit со вставленном id
     */
    PlayerAudit createLog(CreatePlayerAuditDto createPlayerAuditDto);

    /**
     * метод, устанавливающий дату/время авторизации игрока в системе по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    void setLastLogInLog(int playerId) throws AuditException;

    /**
     * метод, устанавливающий дату/время выхода игрока из системы по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    void setLastLogOutLog(int playerId) throws AuditException;

    /**
     * метод, получающий лог по id игрока
     * @param playerId - id игрока
     * @return - объект аудита игрока
     * @throws AuditException
     */
    Optional<PlayerAudit> getLogByPlayerId(int playerId) throws AuditException;
}
