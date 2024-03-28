package live.ioteatime.authservice.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(JwtUtil.class)
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
        assertEquals("test", actual);
    }
}