package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

import static lombok.AccessLevel.PRIVATE;

/**
 * Класс необходимый для инициализации всех сущностей БД
 */
@NoArgsConstructor(access = PRIVATE)
public final class JDBCStarter {
    /**
     * sql запрос на создание всех сущностей БД
     */
    private static String CREATE_SQL = """
            CREATE SCHEMA IF NOT EXISTS wallet_service;
                                    
            CREATE TABLE IF NOT EXISTS wallet_service.player
            (
                id         SERIAL PRIMARY KEY,
                first_name VARCHAR(50) NOT NULL,
                last_name  VARCHAR(50) NOT NULL,
                username   VARCHAR(50) NOT NULL,
                password   VARCHAR(50) NOT NULL,
                UNIQUE (username, password)
            );
                        
            CREATE TABLE IF NOT EXISTS wallet_service.player_account
            (
                account_number BIGINT DEFAULT FLOOR(random()*(10000000000 - 1000000000)) + 10000000000 PRIMARY KEY,
                player_id      INT NOT NULL REFERENCES wallet_service.player (id),
                balance        BIGINT NOT NULL
            );
                        
            CREATE TABLE IF NOT EXISTS wallet_service.player_audit(
                id SERIAL PRIMARY KEY,
                player_id INT NOT NULL REFERENCES wallet_service.player(id),
                last_login TIMESTAMP NOT NULL,
                last_logout TIMESTAMP,
                created_date TIMESTAMP NOT NULL
            );
                        
            CREATE TABLE IF NOT EXISTS wallet_service.transaction(
                id UUID DEFAULT gen_random_uuid() PRIMARY KEY ,
                created_date TIMESTAMP NOT NULL,
                type VARCHAR(10) NOT NULL,
                player_account_number BIGINT NOT NULL REFERENCES wallet_service.player_account (account_number),
                sum INT NOT NULL
            );
            """;

    /**
     * sql запрос на очищение БД
     * используется для интеграционных тестов, чтобы тесты не зависили от состояния БД
     */
    private static String DELETE_SQL = """
        DELETE FROM wallet_service.player_audit;
        DELETE FROM wallet_service.transaction;
        DELETE FROM wallet_service.player_account;
        DELETE FROM wallet_service.player;
    """;


    /**
     * метод, создающий сущности БД
     * @param isTest - параметр для определения, для какого сценария происходит подготовка БД: для тестового или обычного
     */
    public static void prepareDatabase(boolean isTest){
        try (Connection connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
            statement.execute(CREATE_SQL);
            if(isTest){
                statement.execute(DELETE_SQL);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
