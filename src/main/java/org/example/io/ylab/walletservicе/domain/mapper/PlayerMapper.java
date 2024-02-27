package org.example.io.ylab.walletservicе.domain.mapper;

import lombok.NoArgsConstructor;
import org.example.io.ylab.walletservicе.domain.Player;
import org.example.io.ylab.walletservicе.domain.dto.CreatePlayerDto;

import static lombok.AccessLevel.PRIVATE;

/**
 * Класс-маппер для Player
 */
@NoArgsConstructor(access = PRIVATE)
public class PlayerMapper implements Mapper<CreatePlayerDto, Player>{
    /**
     * простейщая реализация singleton класса-маппера
     */
    private static final PlayerMapper INSTANCE = new PlayerMapper();
    public static PlayerMapper getInstance() {
        return INSTANCE;
    }

    /**
     * метод, сопоставляющий dto объект CreatePlayerDto к соответствующей сущности Player
     * @param object - dto объект
     * @return - соответствующая сущность
     */
    @Override
    public Player map(CreatePlayerDto object) {
        return Player.builder()
                .firstName(object.getFirstName())
                .lastName(object.getLastName())
                .username(object.getUsername())
                .password(object.getPassword())
                .build();
    }
}
