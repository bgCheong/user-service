package com.example.userservice.jwt;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    private final Key key;
    private final long accesstokenValidityInMilliseconds;
    private final long refreshtokenValidityInMilliseconds;

    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.access-token-validity-in-seconds}") long accesstokenValidityInSeconds,
                   @Value("${jwt.refresh-token-validity-in-seconds}") long refreshtokenValidityInSeconds) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accesstokenValidityInMilliseconds = accesstokenValidityInSeconds;
        this.refreshtokenValidityInMilliseconds = refreshtokenValidityInSeconds;
    }
    
    public String createAccessToken(String id) {
        return createToken(id, this.accesstokenValidityInMilliseconds);
    }

    public String createRefreshToken(String id) {
        return createToken(id, this.refreshtokenValidityInMilliseconds);
    }
    
    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // MalformedJwtException, ExpiredJwtException 등
            return false;
        }
    }

    // 토큰에서 이메일 정보 추출
    public String getIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String createToken(String username , long validationSeconds) {
//        Date now = new Date();
//        Date validity = new Date(now.getTime() + validationSeconds);
    	
    	Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(validationSeconds , ChronoUnit.SECONDS)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}