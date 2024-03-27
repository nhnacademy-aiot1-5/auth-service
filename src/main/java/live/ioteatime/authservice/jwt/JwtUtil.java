package live.ioteatime.authservice.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SignatureException;
import java.util.Date;
@Slf4j
@Component
public class JwtUtil {
    @Value("${secreet.key}")
    private String secretValue;
    public String createJwt(String userId) {
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;
        log.info(secretValue);

        Date date = new Date();
        //TODO Roles도 clams에 넣기
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setSubject(userId)
                .setIssuedAt(date)
                .setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60 * 24 * 365)))
                .signWith(hs256, secretValue)
                .compact();
    }

    public String getUserId(String jwt) {
        if (jwt.isEmpty()) {
            throw new IllegalArgumentException();
        }
        try {
            Jws<Claims> claimsJwts = Jwts.parser()
                    .setSigningKey(secretValue)
                    .build().parseSignedClaims(jwt);
            return claimsJwts.getBody().getSubject();
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 토큰 {} ", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 않은 토큰 {} ", e.getMessage());
        }
        return null;
    }
}
