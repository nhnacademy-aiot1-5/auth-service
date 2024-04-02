package live.ioteatime.authservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class JwtEncoderTest {
    private String userId;
    private String jwt;
   private String refresh;
    @Autowired
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        userId = "aa";

        jwt = jwtEncoder.createJwt(userId);
        refresh = jwtEncoder.createRefresh();
    }

    @Test
    void createJwtTest() {
        assertFalse(jwt.isEmpty());
        Date now = new Date();

        Claims claims = jwtEncoder.getClaim(jwt);
        Date accessExpiredDate = claims.getExpiration();
        Date expiredDate = new Date(now.getTime() + 1800000);

        assertEquals(accessExpiredDate.toString(),expiredDate.toString());

    }

    @Test
    void testCreateRefresh() {
        Date now = new Date();
        assertNotNull(refresh);

        Claims claims = jwtEncoder.getClaim(refresh);
        Date refreshExpiredDate = claims.getExpiration();
        Date expiredDate = new Date(now.getTime() + 1209600000);

        assertEquals(refreshExpiredDate.toString(),expiredDate.toString());
    }

    @Test
    void testGetClaim() {
        Date now = new Date();

        Claims claims = jwtEncoder.getClaim(jwt);
        Date actual = claims.getExpiration();
        Date expected = new Date(now.getTime() + 1800000);
        assertNotNull(claims);

        assertEquals(expected.toString(),actual.toString());
    }
}
