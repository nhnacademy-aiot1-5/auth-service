package live.ioteatime.authservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 로그인 성공시 돌려줄 Response Dto
 */
@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private String type;
    private String token;
}
