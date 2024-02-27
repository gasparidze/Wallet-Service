package org.example.io.ylab.walletservicе.application_logic.integration;

import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.JDBCStarter;
import org.junit.jupiter.api.BeforeEach;

public abstract class IntegrationTestBase {
    @BeforeEach
    void prepareDatabase() {
        JDBCStarter.prepareDatabase(true);
    }
}
