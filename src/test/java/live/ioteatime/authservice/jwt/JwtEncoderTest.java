package live.ioteatime.authservice.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Slf4j
class JwtEncoderTest {
    private String userId;
    private String jwt;
    @Autowired
    private JwtEncoder jwtEncoder;

    @BeforeEach
    void setUp() {
        userId = "bb";

        jwt = jwtEncoder.createJwt(userId);
    }

    @Test
    void createJwtTest() {
        assertFalse(jwt.isEmpty());
        Date now = new Date();

        Claims claims = jwtEncoder.getClaim(jwt);
        Date accessExpiredDate = claims.getExpiration();

        TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(gmtTimeZone);

        Date expiredDate = new Date(now.getTime() + 3600000);
        assertEquals(sdf.format(accessExpiredDate),sdf.format(expiredDate));

    }

    @Test
    void testGetClaim() {
        Date now = new Date();

        Claims claims = jwtEncoder.getClaim(jwt);
        Date actual = claims.getExpiration();

        TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(gmtTimeZone);

        Date expected = new Date(now.getTime() + 3600000);
        assertNotNull(claims);

        assertEquals(sdf.format(expected),sdf.format(actual));
    }
}
