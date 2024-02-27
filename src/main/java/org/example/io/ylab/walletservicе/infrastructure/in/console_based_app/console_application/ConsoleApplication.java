package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application;

import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.ConnectionManager;

public class ConsoleApplication {
    public static void main(String[] args)  {
        /**
         * подготовка БД
         */
        JDBCStarter.prepareDatabase(false);
        /**
         * точка входа в программу
         */
        try {
            new ApplicationLogic().execute();
        } finally {
            //закрытие connection pool
            ConnectionManager.closePool();
        }
    }
}