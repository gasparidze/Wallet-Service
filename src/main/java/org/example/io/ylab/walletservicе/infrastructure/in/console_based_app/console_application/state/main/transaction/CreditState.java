package org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.main.transaction;

import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;
import org.example.io.ylab.walletservicе.application_services.TransactionServiceI;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.TransactionTypeEnum;
import org.example.io.ylab.walletservicе.domain.dto.CreateTransactionDto;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.ConsoleFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAccountServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.TransactionServiceFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.TransactionStateManagerFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.state.ConsoleState;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.SelectionUtil;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.util.TransactionUtil;

import java.util.Scanner;

public class CreditState implements ConsoleState {
    /**
     * Сервис для работы с транзакциями игрока
     */
    private final TransactionServiceI transactionService;
    /**
     * Сервис для работы со счетом игрока
     */
    private final PlayerAccountServiceI playerAccountService;
    /**
     * Объект сканнера
     */
    private final Scanner scanner;
    /**
     * Объект счета игрока
     */
    private PlayerAccount playerAccount;
    /**
     * следующее состояние приложения
     */
    private ConsoleState nextState;

    public CreditState(PlayerAccount playerAccount){
        this.playerAccount = playerAccount;
        scanner = ConsoleFactory.getScanner();
        transactionService = TransactionServiceFactory.getTransactionService();
        playerAccountService = PlayerAccountServiceFactory.getPlayerService();
        nextState = TransactionStateManagerFactory.getTransactionStateManager(playerAccount.getPlayerId() );
    }

    /**
     *  метод, запускающий логику процесса кредита (пополнения)
     */
    @Override
    public void process() {
        System.out.print("Для того, чтобы сгенерировать уникальный ID транзакции нажмите Enter ");
        SelectionUtil.getValue(() -> scanner.nextLine());
        System.out.print("\nВведите сумму, которую хотели бы получить: ");
        int sum = SelectionUtil.getValue(() -> Integer.parseInt(scanner.nextLine()));

        CreateTransactionDto createTransactionDto
                = TransactionUtil.buildTransactionEntity(TransactionTypeEnum.CREDIT, playerAccount.getAccountNumber(), sum);

        transactionService.doCredit(createTransactionDto);
        System.out.println("Данная сумма " + sum + " успешно зачислена на ваш счет!");

        setNewBalance(sum);
    }

    /**
     * метод, обновляющий баланс счета игрока
     * @param sum - сумма транзакции
     */
    private void setNewBalance(int sum){
        Long currentBalance = playerAccount.getBalance() + sum;
        playerAccount.setBalance(currentBalance);
        playerAccountService.setBalanceByPlayerAccountNumber(
                playerAccount.getAccountNumber(),
                currentBalance
        );
        System.out.println("Текущий баланс составляет: " + currentBalance + " руб.");
    }

    /**
     * метод, возвращаюший слудующее состояние приложения
     * @return - слудующее состояние приложения
     */
    @Override
    public ConsoleState nextState() {
        return nextState;
    }
}
