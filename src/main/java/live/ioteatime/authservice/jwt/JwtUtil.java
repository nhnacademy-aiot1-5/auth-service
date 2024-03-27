package live.ioteatime.authservice.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    public static String createJwt(String userId, String secret) {
        Date date = new Date();
        //TODO Roles도 clams에 넣기
        SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;
        return Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setSubject(userId)
                .setIssuedAt(date)
                .setExpiration(new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60 * 24 * 365)))
                .signWith(hs256, secret)
                .compact();
    }
}
