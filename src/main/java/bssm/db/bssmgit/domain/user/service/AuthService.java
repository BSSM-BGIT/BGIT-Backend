package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.domain.user.web.dto.UserProfile;
import bssm.db.bssmgit.domain.user.web.dto.request.OauthAttributes;
import bssm.db.bssmgit.domain.user.web.dto.response.GitLoginResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.JwtResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.OauthTokenResponse;
import bssm.db.bssmgit.domain.user.web.dto.response.CookieResponseDto;
import bssm.db.bssmgit.global.config.redis.RedisService;
import bssm.db.bssmgit.global.util.CookieProvider;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import bssm.db.bssmgit.global.jwt.JwtProvider;
import bssm.db.bssmgit.global.jwt.JwtValidateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static bssm.db.bssmgit.global.jwt.JwtProperties.REFRESH_TOKEN_VALID_TIME;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final UserFacade userFacade;
    private final JwtProvider jwtProvider;
    private final JwtValidateService jwtValidateService;
    private final CookieProvider cookieProvider;
    private final RedisService redisService;
    private final UserService userService;

    @Value("${spring.security.oauth2.user.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.user.github.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.user.github.redirect-url}")
    private String redirectUrl;

    @Value("${spring.security.oauth2.provider.github.token-uri}")
    private String tokenUrl;

    @Value("${spring.security.oauth2.provider.github.user-info-uri}")
    private String userInfoUrl;

    @Value("${spring.oauth.git.url.token}")
    String token;

    GitHub github;

    @Transactional
    public CookieResponseDto bsmLogin(String authCode) throws IOException {
        User user = userService.bsmOauth(authCode);
        JwtResponseDto jwt = createJwt(user);
        return cookieProvider.jwtToCookies(jwt.getAccessToken(), jwt.getRefreshToken());
    }

    private JwtResponseDto createJwt(User user) {
        final String accessToken = jwtProvider.createAccessToken(user.getEmail());
        final String refreshToken = jwtProvider.createRefreshToken(user.getEmail());
        redisService.setDataExpire(user.getEmail(), refreshToken, REFRESH_TOKEN_VALID_TIME * 1000);

        return new JwtResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public void logout(String accessToken) {
        User user = userFacade.getCurrentUser();
        jwtProvider.logout(user.getEmail(), accessToken);
    }

    @Transactional
    public CookieResponseDto getNewAccessToken(String refreshToken) {
        jwtValidateService.validateRefreshToken(refreshToken);

        String accessToken = jwtProvider.createAccessToken(jwtValidateService.getEmail(refreshToken));
        return cookieProvider.jwtToCookies(accessToken, null);
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
                .uri(tokenUrl)
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

        UserProfile userProfile = getUserProfile(tokenResponse);

        User user = userFacade.getCurrentUser();
        if (user.getGithubId() == null) {

            user.updateGitId(userProfile.getGitId());

            int commits = getUserCommit(userProfile.getGitId());
            String bio = github.getUser(userProfile.getGitId()).getBio();
            String img = github.getUser(userProfile.getGitId()).getAvatarUrl();

            user.updateGitInfo(commits, bio, img);
            userFacade.save(user);
        }


        return new GitLoginResponseDto(user.getGithubId());
    }

    public int getUserCommit(String githubId) {
        userFacade.getCurrentUser();
        try {
            String commits = null;
            boolean b = false;

            URL url = new URL("https://github.com/" + githubId);
            URLConnection uc = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                if (b) {
                    commits = inputLine;
                    break;
                }
                if (inputLine.contains("<h2 class=\"f4 text-normal mb-2\">")) {
                    b = true;
                }
            }

            int commit = 0;
            if (commits != null) {
                commits = commits.replaceAll("\\s+", "");
                commits = commits.replaceAll(",", "");
                commit = Integer.parseInt(commits);
            }

            br.close();

            return commit;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }

    private MultiValueMap<String, String> tokenRequest(String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", redirectUrl);
        return formData;
    }

    private UserProfile getUserProfile(OauthTokenResponse tokenResponse) {
        Map<String, Object> userAttributes = getUserAttributes(tokenResponse);
        return OauthAttributes.extract("github", userAttributes);
    }

    // OAuth 서버에서 유저 정보 map으로 가져오기
    private Map<String, Object> getUserAttributes(OauthTokenResponse tokenResponse) {
        return WebClient.create()
                .get()
                .uri(userInfoUrl)
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }

}
