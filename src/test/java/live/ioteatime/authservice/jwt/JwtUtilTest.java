package live.ioteatime.authservice.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JwtUtilTest {
    private String userId;
    private String jwt;
    @Autowired
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        userId = "test";

        jwt = jwtUtil.createJwt(userId);
    }

    @Test
    void createJwtTest() {
        assertFalse(jwt.isEmpty());
    }

    @Test
    void getUserIdToJwt() {
       String actual= jwtUtil.getUserId(jwt);
        assertTrue("test".equals(actual));
    }
}