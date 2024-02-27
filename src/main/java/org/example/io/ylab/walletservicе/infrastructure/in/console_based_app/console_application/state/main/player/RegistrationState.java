package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.player;

import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;
import org.example.io.ylab.walletservicе.application_services.PlayerAuditServiceI;
import org.example.io.ylab.walletservicе.application_services.PlayerServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.RegistrationException;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAccountDto;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAuditDto;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerDto;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.*;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.ConsoleState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.StateManager;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.SelectionUtil;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Состояние - Регистрация пользователя
 */
public class RegistrationState implements ConsoleState {
    /**
     * Сервис для работы с игроком
     */
    private final PlayerServiceI playerService;
    /**
     * Сервис для работы со счетом игрока
     */
    private final PlayerAccountServiceI playerAccountService;
    /**
     * Сервис для работы с аудитом игрока
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

    public RegistrationState() {
        playerService = PlayerServiceFactory.getPlayerService();
        playerAccountService = PlayerAccountServiceFactory.getPlayerService();
        playerAuditService = PlayerAuditServiceFactory.getPlayerAuditService();
        scanner = ConsoleFactory.getScanner();
    }

    /**
     *  метод, запускающий логику процесса регистрации игрока
     */
    @Override
    public void process(){
        System.out.print("Введите ваше имя: ");
        String firstName = SelectionUtil.getValue(() -> scanner.nextLine());
        System.out.print("Введите вашу фамилию: ");
        String lastName = SelectionUtil.getValue(() -> scanner.nextLine());
        System.out.print("Придумайте username: ");
        String username = SelectionUtil.getValue(() -> scanner.nextLine());
        System.out.print("Придумайте пароль: ");
        String password = SelectionUtil.getValue(() -> scanner.nextLine());
        CreatePlayerDto createPlayerDto = CreatePlayerDto.builder()
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .password(password)
                .build();

        try {
            Player registeredPlayer = playerService.registerNewPlayer(createPlayerDto);
            createPlayerAccount(registeredPlayer);
            createAuditLog(registeredPlayer);
            System.out.println("Игрок " + registeredPlayer.getFirstName() + " " + registeredPlayer.getLastName() + " успешно зарегистрирован!");
            nextState = new StateManager(registeredPlayer.getId());
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e){
            e.getMessage();
        }
    }

    /**
     * метод, создающий объект аудита для игрока
     * @param registeredPlayer - зарегистрированный игрок
     */
    private void createAuditLog(Player registeredPlayer) {
        CreatePlayerAuditDto createPlayerAuditDto = CreatePlayerAuditDto.builder()
                .playerId(registeredPlayer.getId())
                .lastLogin(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .build();
        playerAuditService.createLog(createPlayerAuditDto);
    }

    /**
     * метод, создающий объект счета игрока
     * @param registeredPlayer - зарегистрированный игрок
     */
    private void createPlayerAccount(Player registeredPlayer) {
        CreatePlayerAccountDto createPlayerAccountDto = CreatePlayerAccountDto.builder()
                .playerId(registeredPlayer.getId())
                .balance(0L)
                .transactions(new ArrayList<>())
                .build();
        playerAccountService.createPlayerAccount(createPlayerAccountDto);
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
