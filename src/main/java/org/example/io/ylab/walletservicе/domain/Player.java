package org.example.io.ylab.walletservicе.domain;

import lombok.Builder;
import lombok.Data;

/**
 * Сущность - Игрок
 */
@Data
@Builder
public class Player {
    /**
     * id, генерируемый при вставке экземпляра класса в БД
     */
    private Integer id;
    /**
     * имя игрока
     */
    private String firstName;
    /**
     * фаммилия игрока
     */
    private String lastName;
    /**
     * логин игрока
     */
    private String username;
    /**
     * пароль игрока
     */
    private String password;
}
