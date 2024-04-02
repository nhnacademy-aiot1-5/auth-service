package live.ioteatime.authservice.domain;

import lombok.Data;

@Data
public class TokenWrapper {

    private LoginResponseDto access;
    private LoginResponseDto refresh;
}
