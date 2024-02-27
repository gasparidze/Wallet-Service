package org.example.io.ylab.walletservicе.application_services.exceptions;

/**
 * Exception, выбрасывающийся, если игрок ввел неверный логин или пароль
 */
public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }
}
