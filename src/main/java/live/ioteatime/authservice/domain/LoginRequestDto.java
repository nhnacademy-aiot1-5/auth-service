package live.ioteatime.authservice.domain;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class LoginRequestDto {
    @NotBlank
    private String id;
    @NotBlank
    private String pw;
}
