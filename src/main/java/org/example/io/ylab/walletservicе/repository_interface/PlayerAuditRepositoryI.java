package org.example.io.ylab.walletservicе.repository_interface;

import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.domain.PlayerAudit;

import java.util.Optional;

/**
 * интерфейс, отражающий поведение PlayerAuditRepository
 */
public interface PlayerAuditRepositoryI {
    /**
     * метод, вставляющий объект PlayerAudit в БД
     * @param playerAudit - объект PlayerAudit
     * @return - объект PlayerAudit с сгенерированным id
     */
    PlayerAudit insertLog(PlayerAudit playerAudit);

    /**
     * метод, обновляющий дату/время авторизации игрока в системе по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    void updateLogLastLogInById(int playerId) throws AuditException;

    /**
     * метод, обновляющий дату/время выхода игрока из системы по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    void updateLogLastLogOutById(int playerId) throws AuditException;

    /**
     * метод, запрашивающий лог по id игрока
     * @param playerId - id игрока
     * @return - объект аудита игрока
     * @throws AuditException
     */
    Optional<PlayerAudit> findLogByPlayerId(int playerId) throws AuditException;
}
