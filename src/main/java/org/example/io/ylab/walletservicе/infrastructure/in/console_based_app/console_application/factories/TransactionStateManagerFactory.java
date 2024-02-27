package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.StateManager;

import java.util.HashMap;
import java.util.Map;

import static lombok.AccessLevel.PRIVATE;
/**
 * Данный класс отвечает за создание единственного экземпляра класса StateManager
 */
@NoArgsConstructor(access = PRIVATE)
public class TransactionStateManagerFactory {
    private static Map<Integer, StateManager> transactionStateManagers = new HashMap<>();

    public static StateManager getTransactionStateManager(int playerId) {
        if (!transactionStateManagers.containsKey(playerId)) {
            transactionStateManagers.put(playerId,  new StateManager(playerId));
        }
        return transactionStateManagers.get(playerId);
    }
}
