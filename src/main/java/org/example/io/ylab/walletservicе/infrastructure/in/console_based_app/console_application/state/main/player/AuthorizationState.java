package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.player;

import org.example.io.ylab.walletservicе.application_services.PlayerAuditServiceI;
import org.example.io.ylab.walletservicе.application_services.PlayerServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.AuthorizationException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.ConsoleFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAuditServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.ConsoleState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.MainState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.StateManager;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.SelectionUtil;

import java.util.Optional;
import java.util.Scanner;

/**
 * Состояние - Авторизация пользователя
 */
public class AuthorizationState implements ConsoleState {
    /**
     * Сервис для работы с игроком
     */
    private final PlayerServiceI playerService;
    /**
     * Сервис для работы со счетом игрока
     */
    private final PlayerAuditServiceI playerAuditService;
    /**
     * Объект сканнера
     */
    private final Scanner scanner;
    /**
     * следующее состояние приложения
     */
    private ConsoleState nextState;

    public AuthorizationState() {
        playerService = PlayerServiceFactory.getPlayerService();
        playerAuditService = PlayerAuditServiceFactory.getPlayerAuditService();
        scanner = ConsoleFactory.getScanner();
    }

    /**
     * метод, запускающий логику процесса авторизации игрока
     */
    @Override
    public void process() {
        System.out.println("Введите логин и пароль:");
        System.out.print("Логин: ");
        String username = SelectionUtil.getValue(() -> scanner.nextLine());
        System.out.print("Пароль: ");
        String password = SelectionUtil.getValue(() -> scanner.nextLine());

        try {
            if(playerService.authorizePlayer(username, password)){
                Optional<Player> authorizedPlayer = playerService.getPlayerByUserName(username);

                authorizedPlayer.ifPresent(player -> playerAuditService.setLastLogInLog(player.getId()));
                System.out.println("Вы вошли в систему!");

                nextState = new StateManager(authorizedPlayer.get().getId());
            }
        } catch (AuthorizationException e) {
            System.out.println(e.getMessage());
            System.out.println("1. Повторить попытку\n2. Вернуться назад");
            System.out.print("Выберите необходимый пункт: ");
            int menuSelection = SelectionUtil.getValue(() -> Integer.parseInt(scanner.nextLine()));
            nextState = switch (menuSelection){
                case 1 -> nextState;
                case 2 -> new MainState();
                default -> throw new IllegalStateException("Unexpected value: " + menuSelection);
            };
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
