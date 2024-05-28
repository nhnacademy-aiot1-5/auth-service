package live.ioteatime.authservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 로그인에 필요한 정보를 매핑할 Dto
 */
@Getter
@Setter
@ToString
public class LoginRequestDto {
    @NotBlank
    private String id;
    @NotBlank
    private String pw;
}
