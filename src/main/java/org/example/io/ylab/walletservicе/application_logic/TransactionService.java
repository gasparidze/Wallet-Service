package org.example.io.ylab.walletservicе.application_logic;

import lombok.RequiredArgsConstructor;
import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;
import org.example.io.ylab.walletservicе.application_services.TransactionServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.TransactionException;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.Transaction;
import org.example.io.ylab.walletservicе.domain.dto.CreateTransactionDto;
import org.example.io.ylab.walletservicе.domain.mapper.TransactionMapper;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAccountRepositoryFactory;
import org.example.io.ylab.walletservicе.infrastructure.in.console_based_app.console_application.factories.PlayerAccountServiceFactory;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;
import org.example.io.ylab.walletservicе.repository_interface.TransactionRepositoryI;

import java.util.List;
import java.util.Optional;
/**
 * Сервис для работы с транзакциями игрока
 */
@RequiredArgsConstructor
public class TransactionService implements TransactionServiceI {
    /**
     * объект для обращения к таблице player_account
     */
    private final PlayerAccountServiceI playerAccountService;
    /**
     * объект для обращения к таблице transaction
     */
    private final TransactionRepositoryI transactionRepository;
    /**
     * mapper, сопоставляющий соответствующий dto объект с entity объектом
     */
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepositoryI transactionRepository) {
        this.transactionRepository = transactionRepository;
        this.playerAccountService = PlayerAccountServiceFactory.getPlayerService();
        this.transactionMapper = TransactionMapper.getInstance();
    }

    /**
     * метод, создающий транзакцию на снятие денежных средств со счета
     * @param createDebitTransaction - dto объект
     * @throws TransactionException
     */
    @Override
    public void doDebit(CreateTransactionDto createDebitTransaction) throws TransactionException {
        Optional<PlayerAccount> playerAccount = playerAccountService
                .getAccountByNumber(createDebitTransaction.getPlayerAccountNumber());
        playerAccount.ifPresent(account -> {
            if(account.getBalance() - createDebitTransaction.getSum() < 0) {
                throw new TransactionException("На счету недостаточно средств!");
            }
        });

        Transaction debitTransaction = transactionMapper.map(createDebitTransaction);
        transactionRepository.createTransaction(debitTransaction);
    }

    /**
     * метод, создающий транзакцию на получение денежных средств со счета
     * @param createCreditTransaction - dto объект
     */
    @Override
    public void doCredit(CreateTransactionDto createCreditTransaction) {
        Transaction creditTransaction = transactionMapper.map(createCreditTransaction);
        transactionRepository.createTransaction(creditTransaction);
    }

    /**
     * метод, получающий список транзакций по номеру счета
     * @param accountNumber - номер счета
     * @return - список транзакций
     */
    @Override
    public List<Transaction> getTransactionsByAccountNumber(Long accountNumber) {
        return transactionRepository.findTransactionsByAccountNumber(accountNumber);
    }
}
