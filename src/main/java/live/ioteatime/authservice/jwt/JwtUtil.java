package live.ioteatime.authservice.jwt;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;

@Slf4j
public class JwtUtil {
    @Value("${secreet.key}")
    private String secretValue;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretValue.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(String userId) {
        Claims claims = Jwts.claims().setSubject(userId);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(Duration.ofHours(3).toMillis()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserId(String jwt) {
        if (jwt.isEmpty()) {
            throw new IllegalArgumentException();
        }
        try {
            Claims claimsJwts = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claimsJwts.getSubject();
        } catch (SecurityException | MalformedJwtException e) {
            log.error("유효하지 않은 토큰 {} ", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 않은 토큰 {} ", e.getMessage());
        }
        return null;
    }
}
