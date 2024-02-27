package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository.PlayerAuditJdbcRepository;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAuditRepositoryI;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса PlayerAuditJdbcRepository
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerAuditRepositoryFactory {
    private static PlayerAuditRepositoryI playerAuditRepositoryInstance;

    public static PlayerAuditRepositoryI getPlayerAuditRepository() {
        if (playerAuditRepositoryInstance == null) {
            playerAuditRepositoryInstance = new PlayerAuditJdbcRepository();
        }
        return playerAuditRepositoryInstance;
    }
}
