package live.ioteatime.authservice.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.util.Date;

@Slf4j
public class JwtEncoder {
    @Value("${secret.key.get}")
    private String secretValue;

    public String createJwt(String userId) {

        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Duration.ofHours(3).toMillis()))
                .signWith(SignatureAlgorithm.HS256,secretValue)
                .compact();
    }

    public String getUserId(String jwt) {
        if (jwt.isEmpty()) {
            throw new IllegalArgumentException();
        }
        try {
            Claims claimsJwts = Jwts.parserBuilder()
                    .setSigningKey(secretValue)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claimsJwts.getSubject();
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 토큰 {} ", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 토큰 {} ", e.getMessage());
        }
        return null;
    }
}
