package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.application_logic.PlayerAuditService;
import org.example.io.ylab.walletservicе.application_services.PlayerAuditServiceI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса PlayerAuditService
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerAuditServiceFactory {
    private static PlayerAuditServiceI playerAuditServiceInstance;

    public static PlayerAuditServiceI getPlayerAuditService() {
        if (playerAuditServiceInstance == null) {
            playerAuditServiceInstance = new PlayerAuditService(
                    PlayerAuditRepositoryFactory.getPlayerAuditRepository()
            );
        }
        return playerAuditServiceInstance;
    }
}
