package live.ioteatime.authservice.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
class JwtEncoderTest {
    private String userId;
    private String jwt;
    @Autowired
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        userId = "aa";

        jwt = jwtEncoder.createJwt(userId);
    }

    @Test
    void createJwtTest() {
        assertFalse(jwt.isEmpty());
    }

    @Test
    void getUserIdToJwt() {
       String actual= jwtEncoder.getUserId(jwt);
        assertEquals("aa", actual);
    }
}
