package org.example.io.ylab.walletservicе.application_services.exceptions;

/**
 * Exception, выбрасывающийся, если у игрока недостаточно средств на счете при снятии
 */
public class TransactionException extends RuntimeException{
    public TransactionException(String message) {
        super(message);
    }
}
