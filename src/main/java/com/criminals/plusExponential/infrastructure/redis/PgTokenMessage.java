package com.criminals.plusExponential.infrastructure.redis;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PgTokenMessage implements Serializable {
    private Long userId;
    private String pgToken;
}
