package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository.PlayerAccountJdbcRepository;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса PlayerAccountJdbcRepository
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerAccountRepositoryFactory {
    private static PlayerAccountRepositoryI playerAccountRepositoryInstance;

    public static PlayerAccountRepositoryI getAccountPlayerRepository() {
        if (playerAccountRepositoryInstance == null) {
            playerAccountRepositoryInstance = new PlayerAccountJdbcRepository();
        }
        return playerAccountRepositoryInstance;
    }
}
