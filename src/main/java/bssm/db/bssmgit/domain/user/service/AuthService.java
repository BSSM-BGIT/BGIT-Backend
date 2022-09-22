package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.UserProfile;
import bssm.db.bssmgit.domain.user.web.dto.request.OauthAttributes;
import bssm.db.bssmgit.domain.user.web.dto.response.GitLoginResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.OauthTokenResponse;
import bssm.db.bssmgit.domain.user.web.dto.response.TokenResponseDto;
import bssm.db.bssmgit.global.config.redis.RedisService;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import bssm.db.bssmgit.global.jwt.JwtTokenProvider;
import bssm.db.bssmgit.global.jwt.JwtValidateService;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static bssm.db.bssmgit.global.jwt.JwtProperties.REFRESH_TOKEN_VALID_TIME;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtValidateService jwtValidateService;
    private final RedisService redisService;
    private final UserService userService;

    @Value("${spring.security.oauth2.user.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.user.github.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.user.github.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.provider.github.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.provider.github.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.oauth.git.url.token}")
    String token;

    GitHub github;

    @Transactional
    public TokenResponseDto bsmLogin(String authCode) throws IOException {
        User user = userService.bsmOauth(authCode);

        final String accessToken = jwtTokenProvider.createAccessToken(user.getEmail());
        final String refreshToken = jwtTokenProvider.createRefreshToken(user.getEmail());
        redisService.setDataExpire(user.getEmail(), refreshToken, REFRESH_TOKEN_VALID_TIME);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public void logout(String accessToken) {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        jwtTokenProvider.logout(user.getEmail(), accessToken);
    }

    @Transactional
    public TokenResponseDto getNewAccessToken(String refreshToken) {
        jwtValidateService.validateRefreshToken(refreshToken);

        return TokenResponseDto.builder()
                .accessToken(jwtTokenProvider.createAccessToken(
                        jwtValidateService.getEmail(refreshToken)))
                .build();
    }

    @Transactional
    public GitLoginResponseDto gitLogin(String code) throws IOException {
        try {
            connectToGithub(token);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GIT_CONNECTION_REFUSED);
        }

        OauthTokenResponse tokenResponse = WebClient.create()
                .post()
                .uri(tokenUri)
                .headers(header -> {
                    header.setBasicAuth(clientId, clientSecret);
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(tokenRequest(code))
                .retrieve()
                .bodyToMono(OauthTokenResponse.class)
                .block();

        UserProfile userProfile = getUserProfile("github", tokenResponse);

        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        if (user.getGithubId() == null) {
            user.updateGitId(userProfile.getGitId());

            int commits = github.searchCommits().author(user.getGithubId())
                    .list().getTotalCount();
            String bio = github.getUser(user.getGithubId()).getBio();
            String img = github.getUser(user.getGithubId()).getAvatarUrl();

            user.updateGitInfo(commits, bio, img);
        }

        return new GitLoginResponseDto(user.getGithubId());
    }

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }

    private MultiValueMap<String, String> tokenRequest(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", redirectUri);
        return formData;
    }

    private UserProfile getUserProfile(String providerName, OauthTokenResponse tokenResponse) {
        Map<String, Object> userAttributes = getUserAttributes(tokenResponse);
        return OauthAttributes.extract(providerName, userAttributes);
    }

    // OAuth 서버에서 유저 정보 map으로 가져오기
    private Map<String, Object> getUserAttributes(OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(userInfoUri)
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

}
