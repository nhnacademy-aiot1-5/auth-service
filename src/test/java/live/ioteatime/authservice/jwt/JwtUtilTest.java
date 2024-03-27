package live.ioteatime.authservice.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
@SpringBootTest
class JwtUtilTest {

    @Value("${secreet.key}")
    private String secret;
    @Test
    void createJwtTest() {

        String userId = "test";

        String jwt = JwtUtil.createJwt(userId, secret);

        assertFalse(jwt.isEmpty());
    }

}