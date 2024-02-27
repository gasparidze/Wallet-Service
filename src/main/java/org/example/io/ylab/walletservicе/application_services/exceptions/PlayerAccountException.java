package org.example.io.ylab.walletservicе.application_services.exceptions;

/**
 * Exception, выбрасывающийся, если у игрока отсутствует счет
 */
public class PlayerAccountException extends RuntimeException{
    public PlayerAccountException(String message) {
        super(message);
    }
}
