package org.example.io.ylab.walletservicе.application_services.exceptions;

/**
 * Exception, выбрасывающийся, если у игрока отсутствуют логи
 */
public class AuditException extends RuntimeException {
    public AuditException(String message) {
        super(message);
    }
}
