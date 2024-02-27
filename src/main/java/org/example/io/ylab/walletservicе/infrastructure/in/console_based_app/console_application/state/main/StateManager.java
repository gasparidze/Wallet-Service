package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main;

import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;
import org.example.io.ylab.walletservicе.application_services.PlayerAuditServiceI;
import org.example.io.ylab.walletservicе.application_services.PlayerServiceI;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.ConsoleFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAccountServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAuditServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.ConsoleState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.transaction.CreditState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.transaction.DebitState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.SelectionUtil;

import java.util.Scanner;

/**
 * Класс, управляющий сменами состояний приложения
 */
public class StateManager implements ConsoleState {
    /**
     * Сервис для работы со счетом игрока
     */
    private final PlayerAccountServiceI playerAccountService;
    /**
     * Сервис для работы с игроком
     */
    private final PlayerServiceI playerService;
    /**
     * Сервис для работы с аудитом игрока
     */
    private final PlayerAuditServiceI playerAuditService;
    /**
     * Объект сканнера
     */
    private final Scanner scanner;
    /**
     * id игрока
     */
    private final int playerId;
    /**
     * следующее состояние приложения
     */
    private ConsoleState nextState;


    public StateManager(int playerId) {
        playerAccountService = PlayerAccountServiceFactory.getPlayerService();
        playerService = PlayerServiceFactory.getPlayerService();
        playerAuditService = PlayerAuditServiceFactory.getPlayerAuditService();
        scanner = ConsoleFactory.getScanner();
        this.playerId = playerId;
    }

    /**
     * метод, запускающий логику процесса смены состояний приложения
     */
    @Override
    public void process() {
        System.out.println("1. Посмотреть текущий баланс\n2. Просмотреть историю транзакций\n3. Дебит" +
                "\n4. Кредит\n5. Посмотреть аудит действий\n6. Выйти из системы");
        System.out.print("Выберите нужную операцию:");
        int menuSelection = SelectionUtil.getValue(() -> Integer.parseInt(scanner.nextLine()));

        PlayerAccount playerAccount = playerAccountService.getAccountByPlayerId(playerId).get();

        switch (menuSelection) {
            case 1 -> {
                Long balance = playerAccountService.getBalanceByPlayerId(playerId);
                System.out.println("Ваш текущий баланс: " + balance + " руб.");
                nextState = this;
            }
            case 2 -> nextState = new TransactionHistoryState(playerAccount);
            case 3 -> nextState = new DebitState(playerAccount);
            case 4 -> nextState = new CreditState(playerAccount);
            case 5 -> {
                System.out.println(playerAuditService.getLogByPlayerId(playerId).get() + "\n");
                nextState = this;
            }
            case 6 -> {
                playerAuditService.setLastLogOutLog(playerId);
                System.out.println("Вы вышли из системы");
                nextState = new MainState();
            }
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
