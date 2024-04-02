package live.ioteatime.authservice.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * jwt 토큰을 만드는 클래스
 */
@Slf4j
public class JwtEncoder {
    /**
     * @secretValue 토큰을 만들기 위한 시크릿키
     * instance sever 존재하는 application-prod.properties 에 존재하며
     * 스크립트 실행시 properties 교체
     */
    @Value("${secret.key.get}")
    private String secretValue;

    /**
     *
     * @param userId 토큰에 들어갈 유저의 아이디
     * @return String 형의 토큰
     * 제목을 userId로 지정해준 이유
     *  - getSubject 사용해서 바로 가져올수있다.
     *  토큰 유효기간은 3시간
     *  알고리즘에 시크릿키를 넣어 암호화
     *  유효기간 30분
     */
    public String createJwt(String userId) {

        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + 3600000))
                .signWith(SignatureAlgorithm.HS256,secretValue)
                .compact();
    }

    /**
     *  토큰 안의 Claims 반환
     * @param jwt 토큰값
     * @return 토큰 값 안의 Claims 정보
     */
    public Claims getClaim(String jwt) {
        return Jwts.parserBuilder().setSigningKey(secretValue).build().parseClaimsJws(jwt).getBody();
    }

}
