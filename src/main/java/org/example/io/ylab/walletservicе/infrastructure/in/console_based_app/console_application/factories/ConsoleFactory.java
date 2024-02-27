package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories;

import lombok.NoArgsConstructor;

import java.util.Scanner;

import static lombok.AccessLevel.PRIVATE;

/**
 * Данный класс отвечает за создание единственного экземпляра класса Scanner
 */
@NoArgsConstructor(access = PRIVATE)
public class ConsoleFactory {
    private static Scanner scanner;

    public static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }
}
