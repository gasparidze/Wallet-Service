package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application;

import org.example.io.ylab.walletservicе.application_services.exceptions.RegistrationException;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.ConsoleState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.MainState;

/**
 * Класс, реализующий смену состояний консольного приложения
 */
public class ApplicationLogic {
    /**
     * состояние консольного приложения
     */
    private ConsoleState currentState;

    public ApplicationLogic() throws RegistrationException {
        this.currentState = new MainState();
    }

    /**
     * запуск состояний консольного приложения
     */
    public void execute() {
        while (true) {
            try {
                currentState.process();
                if (currentState.nextState() != null) {
                    ConsoleState nextState = currentState.nextState();
                    currentState = nextState;
                }
            } catch (Exception e) {
                System.err.println("Problem with input: " + e.getMessage());
                System.exit(1);
            }
        }
    }
}
