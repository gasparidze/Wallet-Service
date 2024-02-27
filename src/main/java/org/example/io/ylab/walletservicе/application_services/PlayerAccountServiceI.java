package org.example.io.ylab.walletservicе.application_services;

import org.example.io.ylab.walletservicе.application_services.exceptions.PlayerAccountException;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAccountDto;

import java.util.Optional;

/**
 * интерфейс, отражающий поведение PlayerAccountService
 */
public interface PlayerAccountServiceI {
    /**
     * метод, создающий объект PlayerAccount
     * @param createPlayerAccountDto - dto объект
     * @return - объект PlayerAccount с сгенерированным номером счета (accountNumber)
     */
    PlayerAccount createPlayerAccount(CreatePlayerAccountDto createPlayerAccountDto);

    /**
     * метод, получающий баланс счета по id игрока
     * @param playerId - id игрока
     * @return - баланс игрока
     */
    Long getBalanceByPlayerId(int playerId);

    /**
     * метод, получающий счет игрока по id игрока
     * @param playerId - id игрока
     * @return - объект счета игрока
     * @throws PlayerAccountException
     */
    Optional<PlayerAccount> getAccountByPlayerId(int playerId) throws PlayerAccountException;

    /**
     * метод, устанавливающий баланс счета по номеру счета
     * @param accountNumber - счет игрока
     * @param balance - баланс счета
     */
    void setBalanceByPlayerAccountNumber(Long accountNumber, Long balance);

    /**
     * метод, получающий счет игрока по номеру счета
     * @param accountNumber - номер счета
     * @return - объект счета игрока
     */
    Optional<PlayerAccount> getAccountByNumber(Long accountNumber);
}
