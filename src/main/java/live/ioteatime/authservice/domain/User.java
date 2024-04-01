package live.ioteatime.authservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 정보를 매핑할수있는 Dto
 */
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class User {
    private String id;
    private String pw;
}
