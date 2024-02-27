package org.example.io.ylab.walletservicе.domain.dto;

import lombok.Builder;
import lombok.Value;

/**
 * dto объект сущности Player
 */
@Value
@Builder
public class CreatePlayerDto {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
}
