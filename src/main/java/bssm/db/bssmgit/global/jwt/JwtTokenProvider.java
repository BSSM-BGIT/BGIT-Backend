package bssm.db.bssmgit.global.jwt;

import bssm.db.bssmgit.global.config.redis.RedisService;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

import static bssm.db.bssmgit.global.jwt.JwtProperties.JWT_ACCESS;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final RedisService redisService;

    @Value("${spring.security.jwt.secret}")
    private String secretKey;

    @Value("${spring.security.jwt.blacklist.access-token}")
    private String blackListATPrefix;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email, long time) {
        Claims claims = Jwts.claims();
        claims.put("email", email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + time))
                .signWith(getSigningKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createAccessToken(String email) {
        return createToken(email, JwtProperties.ACCESS_TOKEN_VALID_TIME);
    }

    public String createRefreshToken(String email) {
        return createToken(email, JwtProperties.REFRESH_TOKEN_VALID_TIME);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(JWT_ACCESS);
    }

    public void logout(String email, String accessToken) {
        long expiredAccessTokenTime = getExpiredTime(accessToken)
                .getTime() - new Date().getTime();

        redisService.setValues(blackListATPrefix + accessToken, email, Duration.ofMillis(expiredAccessTokenTime));
        redisService.deleteData(email);

        redisService.setBlackList(accessToken, "ACCESS-TOKEN", expiredAccessTokenTime);
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date getExpiredTime(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody().getExpiration();
    }

    public Claims extractAllClaims(String token) {
        try {
            if(redisService.hasKeyBlackList(token)) {
                throw new CustomException(ErrorCode.USER_NOT_LOGIN);
            }

            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey(secretKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.EXPIRED_TOKEN);
        }
    }
}
