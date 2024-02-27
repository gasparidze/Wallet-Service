package org.example.io.ylab.walletservicе.application_logic;

import lombok.RequiredArgsConstructor;
import org.example.io.ylab.walletservicе.application_services.PlayerAuditServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuditException;
import org.example.io.ylab.walletservicе.domain.PlayerAudit;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAuditDto;
import org.example.io.ylab.walletservicе.domain.mapper.PlayerAuditMapper;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAuditRepositoryI;

import java.util.Optional;
/**
 * Сервис для работы с аудитом игрока
 */
@RequiredArgsConstructor
public class PlayerAuditService implements PlayerAuditServiceI {
    /**
     * объект для обращения к таблице player_audit
     */
    private final PlayerAuditRepositoryI playerAuditRepository;
    /**
     * mapper, сопоставляющий соответствующий dto объект с entity объектом
     */
    private final PlayerAuditMapper playerAuditMapper;

    public PlayerAuditService(PlayerAuditRepositoryI playerAuditRepository) {
        this.playerAuditRepository = playerAuditRepository;
        this.playerAuditMapper = PlayerAuditMapper.getInstance();
    }

    /**
     * метод, создающий объект PlayerAudit
     * @param createPlayerAuditDto - dto объект
     * @return - объект PlayerAudit со вставленном id
     */
    @Override
    public PlayerAudit createLog(CreatePlayerAuditDto createPlayerAuditDto) {
        PlayerAudit playerAudit = playerAuditMapper.map(createPlayerAuditDto);
        return playerAuditRepository.insertLog(playerAudit);
    }

    /**
     * метод, устанавливающий дату/время авторизации игрока в системе по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    @Override
    public void setLastLogInLog(int playerId) throws AuditException {
        playerAuditRepository.updateLogLastLogInById(playerId);
    }

    /**
     * метод, устанавливающий дату/время выхода игрока из системы по его id
     * @param playerId - id игрока
     * @throws AuditException
     */
    @Override
    public void setLastLogOutLog(int playerId) throws AuditException {
        playerAuditRepository.updateLogLastLogOutById(playerId);
    }

    /**
     * метод, получающий лог по id игрока
     * @param playerId - id игрока
     * @return - объект аудита игрока
     * @throws AuditException
     */
    @Override
    public Optional<PlayerAudit> getLogByPlayerId(int playerId) throws AuditException {
        Optional<PlayerAudit> playerAudit = playerAuditRepository.findLogByPlayerId(playerId);
        if(!playerAudit.isPresent()){
            throw new AuditException("У данного пользователя отсутствуют логи");
        }

        return playerAudit;
    }
}
