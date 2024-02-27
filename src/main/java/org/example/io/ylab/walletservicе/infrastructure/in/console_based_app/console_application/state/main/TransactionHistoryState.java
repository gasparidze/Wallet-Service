package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main;

import org.example.io.ylab.walletservicе.application_services.TransactionServiceI;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.Transaction;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.TransactionServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.TransactionStateManagerFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.ConsoleState;

import java.util.List;
/**
 * Класс, реализующий логику историй транзакций счета игрока
 */
public class TransactionHistoryState implements ConsoleState {
    /**
     * Сервис для работы со счетом игрока
     */
    private PlayerAccount playerAccount;
    /**
     * Сервис для работы с транзакциями игрока
     */
    private final TransactionServiceI transactionService;
    /**
     * следующее состояние приложения
     */
    private final ConsoleState nextState;

    public TransactionHistoryState(PlayerAccount playerAccount){
        this.playerAccount = playerAccount;
        transactionService = TransactionServiceFactory.getTransactionService();
        nextState = TransactionStateManagerFactory.getTransactionStateManager(playerAccount.getPlayerId());
    }

    /**
     *  метод, запускающий логику процесса истории транзакций счета игрока
     */
    @Override
    public void process() {
        List<Transaction> playerTransactions =
                transactionService.getTransactionsByAccountNumber(playerAccount.getAccountNumber());
        if (!playerTransactions.isEmpty()) {
            playerAccount.setTransactions(playerTransactions);

            System.out.println("Ваши операции:");
            playerTransactions.forEach(el -> {
                int i = 0;
                System.out.println(++i + ". " + el);
            });
            System.out.println("\n");
        } else {
            System.out.println("Список транзакций пуст");
        }
    }

    /**
     * метод, возвращаюший слудующее состояние приложения
     * @return - слудующее состояние приложения
     */
    @Override
    public ConsoleState nextState() {
        return nextState;
    }
}
