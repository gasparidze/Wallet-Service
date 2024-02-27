package org.example.io.ylab.walletservicе.application_logic;

import lombok.RequiredArgsConstructor;
import org.example.io.ylab.walletservicе.application_services.PlayerAccountServiceI;
import org.example.io.ylab.walletservicе.application_services.exceptions.PlayerAccountException;;
import org.example.io.ylab.walletservicе.domain.PlayerAccount;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerAccountDto;
import org.example.io.ylab.walletservicе.domain.mapper.PlayerAccountMapper;
import org.example.io.ylab.walletservicе.repository_interface.PlayerAccountRepositoryI;

import java.util.Optional;

/**
 * Сервис для работы со счетом игрока
 */
@RequiredArgsConstructor
public class PlayerAccountService implements PlayerAccountServiceI {
    /**
     * объект для обращения к таблице player_account
     */
    private final PlayerAccountRepositoryI playerAccountRepository;
    /**
     * mapper, сопоставляющий соответствующий dto объект с entity объектом
     */
    private final PlayerAccountMapper playerAccountMapper;

    public PlayerAccountService(PlayerAccountRepositoryI playerAccountRepository) {
        this.playerAccountRepository = playerAccountRepository;
        this.playerAccountMapper = PlayerAccountMapper.getInstance();
    }

    /**
     *
     * метод, создающий объект PlayerAccount
     * @param createPlayerAccountDto - dto объект
     * @return - объект PlayerAccount с сгенерированным номером счета (accountNumber)
     */
    @Override
    public PlayerAccount createPlayerAccount(CreatePlayerAccountDto createPlayerAccountDto){
        PlayerAccount playerAccount = playerAccountMapper.map(createPlayerAccountDto);
        return playerAccountRepository.insertPlayerAccount(playerAccount);
    }

    /**
     * метод, получающий баланс счета по id игрока
     * @param playerId - id игрока
     * @return - баланс игрока
     */
    @Override
    public Long getBalanceByPlayerId(int playerId) {
        return getAccountByPlayerId(playerId).get().getBalance();
    }

    /**
     * метод, получающий счет игрока по id игрока
     * @param playerId - id игрока
     * @return - объект счета игрока
     * @throws PlayerAccountException
     */
    @Override
    public Optional<PlayerAccount> getAccountByPlayerId(int playerId) throws PlayerAccountException {
        Optional<PlayerAccount> playerAccount = playerAccountRepository.findAccountByPlayerId(playerId);
        if(!playerAccount.isPresent()){
            throw new PlayerAccountException("У данного клиента нет счета");
        }
        return playerAccount;
    }

    /**
     * метод, устанавливающий баланс счета по номеру счета
     * @param accountNumber - счет игрока
     * @param balance - баланс счета
     */
    @Override
    public void setBalanceByPlayerAccountNumber(Long accountNumber, Long balance) {
        playerAccountRepository.updateBalanceByAccountNumber(accountNumber, balance);
    }

    /**
     * метод, получающий счет игрока по номеру счета
     * @param accountNumber - номер счета
     * @return - объект счета игрока
     */
    @Override
    public Optional<PlayerAccount> getAccountByNumber(Long accountNumber) {
        return playerAccountRepository.findAccountByNumber(accountNumber);
    }
}
