package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.jdbc_repository;

import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.ConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionRunner {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = ConnectionManager.get()) {

        }
    }
}
