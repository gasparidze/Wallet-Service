package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.application_logic.PlayerAccountService;
import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса PlayerAccountService
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerAccountServiceFactory {
    private static PlayerAccountServiceI playerAccountServiceInstance;

    public static PlayerAccountServiceI getPlayerService() {
        if (playerAccountServiceInstance == null) {
            playerAccountServiceInstance = new PlayerAccountService(
                    PlayerAccountRepositoryFactory.getAccountPlayerRepository()
            );
        }
        return playerAccountServiceInstance;
    }
}
