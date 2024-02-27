package org.example.io.ylab.walletservicе.repository_interface;

import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import java.util.Optional;

/**
 * интерфейс, отражающий поведение PlayerAccountRepository
 */
public interface PlayerAccountRepositoryI {
    /**
     * метод, вставляющий объект PlayerAccount в БД
     * @param playerAccount - объект PlayerAccount
     * @return - объект PlayerAccount с сгенерированным номером счета (accountNumber)
     */
    PlayerAccount insertPlayerAccount(PlayerAccount playerAccount);
    /**
     * метод, запрашивающий счет игрока по его id из БД
     * @param playerId - id игрока
     * @return - баланс игрока
     */
    Optional<PlayerAccount> findAccountByPlayerId(int playerId);

    /**
     * метод, обновляющий баланс счета по номеру счета
     * @param accountNumber - счет игрока
     * @param balance - баланс счета
     */
    void updateBalanceByAccountNumber(Long accountNumber, Long balance);

    /**
     * метод, запрашивающий счет игрока по номеру счета
     * @param accountNumber - номер счета
     * @return - объект счета игрока
     */
    Optional<PlayerAccount> findAccountByNumber(Long accountNumber);
}