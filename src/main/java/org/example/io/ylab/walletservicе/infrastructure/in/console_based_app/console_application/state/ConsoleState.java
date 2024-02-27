package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state;

/**
 * Класс, реализующий запуск логики состояний консольного приложения
 */
public interface ConsoleState {
    /**
     * метод, запускающий логику состояний приложения
     * @throws Exception
     */
    void process() throws Exception;

    /**
     * метод, возвращающий слудующее состояние приложения
     * @return - слудующее состояние приложения
     */
    ConsoleState nextState();
}
