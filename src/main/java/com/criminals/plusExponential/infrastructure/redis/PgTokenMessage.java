package com.criminals.plusExponential.infrastructure.redis;

import lombok.Getter;
import lombok.Setter;

@Getter
public class PgTokenMessage {
    private Long userId;
    private String pgToken;

    public PgTokenMessage(Long userId, String pgToken) {
        this.userId = userId;
        this.pgToken = pgToken;
    }
}
