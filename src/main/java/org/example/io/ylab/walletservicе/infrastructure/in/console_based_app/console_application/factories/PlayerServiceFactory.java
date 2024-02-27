package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.application_logic.PlayerService;
import org.example.io.ylab.walletservicе.application_services.PlayerServiceI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса PlayerService
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerServiceFactory {
    private static PlayerServiceI playerServiceInstance;

    public static PlayerServiceI getPlayerService() {
        if (playerServiceInstance == null) {
            playerServiceInstance = new PlayerService(PlayerRepositoryFactory.getPlayerRepository());
        }
        return playerServiceInstance;
    }
}
