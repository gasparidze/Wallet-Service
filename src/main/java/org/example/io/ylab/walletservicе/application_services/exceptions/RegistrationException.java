package org.example.io.ylab.walletservicе.application_services.exceptions;

/**
 * Exception, выбрасывающийся, если игрок уже зарегистрирован в системе
 */
public class RegistrationException extends RuntimeException{
    public RegistrationException(String message) {
        super(message);
    }
}
