package bssm.db.bssmgit.global.jwt;

import bssm.db.bssmgit.global.config.redis.RedisService;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtValidateService {

    private final JwtProvider jwtProvider;
    private final RedisService redisService;

    public String getEmail(String token) {
        return jwtProvider.extractAllClaims(token).get("email", String.class);
    }

    public void validateRefreshToken(String token) {
        if (redisService.getData(getEmail(token)) == null) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

}