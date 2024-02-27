package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main;

import org.example.io.ylab.walletservicе.application_services.exceptions.RegistrationException;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.player.AuthorizationState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.ConsoleFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.player.RegistrationState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.ConsoleState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.SelectionUtil;

import java.util.Scanner;

/**
 * Начальное состояние приложения
 */
public class MainState implements ConsoleState {
    /**
     * Объект сканнера
     */
    private final Scanner scanner;
    /**
     * следующее состояние приложения
     */
    private ConsoleState nextState;

    public MainState() throws RegistrationException {
        scanner = ConsoleFactory.getScanner();
    }

    /**
     * метод, запускающий логику начального состояния приложения
     * @throws Exception
     */
    @Override
    public void process() {
        System.out.println("1. Зарегистрироваться\n2. Войти\n");
        System.out.print("Введите нужный вариант: ");

        int menuSelection = SelectionUtil.getValue(() -> Integer.parseInt(scanner.nextLine()));

        switch (menuSelection) {
            case 1 -> nextState = new RegistrationState();
            case 2 -> nextState = new AuthorizationState();
        }
    }

    /**
     * метод, возвращающий слудующее состояние приложения
     * @return - слудующее состояние приложения
     */
    @Override
    public ConsoleState nextState() {
        return nextState;
    }
}
