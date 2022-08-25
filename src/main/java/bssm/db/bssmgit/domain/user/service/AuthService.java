package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.LoginRequestDto;
import bssm.db.bssmgit.domain.user.web.dto.TokenResponseDto;
import bssm.db.bssmgit.global.config.redis.RedisService;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import bssm.db.bssmgit.global.jwt.JwtTokenProvider;
import bssm.db.bssmgit.global.jwt.JwtValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static bssm.db.bssmgit.global.jwt.JwtProperties.REFRESH_TOKEN_VALID_TIME;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtValidateService jwtValidateService;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;

    public TokenResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.matchedPassword(passwordEncoder, user, request.getPassword());

        final String accessToken = jwtTokenProvider.createAccessToken(request.getEmail());
        final String refreshToken = jwtTokenProvider.createRefreshToken(request.getEmail());
        redisService.setDataExpire(request.getEmail(), refreshToken, REFRESH_TOKEN_VALID_TIME);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public void logout(String accessToken) {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        jwtTokenProvider.logout(user.getEmail(), accessToken);
    }

    public TokenResponseDto getNewAccessToken(String refreshToken) {
        jwtValidateService.validateRefreshToken(refreshToken);

        return TokenResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(
                        jwtValidateService.getEmail(refreshToken)))
                .build();
    }
}
