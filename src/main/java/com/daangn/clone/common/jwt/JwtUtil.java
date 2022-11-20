package com.daangn.clone.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.password}")
    private String secretKey;

    /** JWT 토큰 생성 메서드 */
    public String createToken(String subject){

        //만료 기간은 1일으로 설정
        Date now = new Date();
        Date expiration = new Date(now.getTime() + Duration.ofDays(1).toMillis());

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer("Spring_Sever") // 토큰 발급자
                .setIssuedAt(now) // 토큰 발급 시간
                .setExpiration(expiration) //만료 시간
                .setSubject(subject) //토큰 제목
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8))) //이 알고리즘과 , 이 시크릿 키로 서명
                .compact();
    }

    /** JWT 토큰의 유효성 체크 메서드 */
    public Claims parseJwtToken(String token){
        String removedBearerToken = BearerRemove(token); //Bearer 제거

        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                .parseClaimsJws(token)
                .getBody();

    }

    private String BearerRemove(String token){
        return token.substring("Bearer ".length());
    }

}
