package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository.PlayerJdbcRepository;
import org.example.io.ylab.walletservicе.repository_interface.PlayerRepositoryI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса PlayerJdbcRepository
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerRepositoryFactory extends PlayerJdbcRepository {
    private static PlayerRepositoryI playerRepositoryInstance;

    public static PlayerRepositoryI getPlayerRepository() {
        if (playerRepositoryInstance == null) {
            playerRepositoryInstance = new PlayerJdbcRepository();
        }
        return playerRepositoryInstance;
    }
}
