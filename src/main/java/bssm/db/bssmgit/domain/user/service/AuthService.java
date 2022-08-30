package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.GitIdResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.TokenResponseDto;
import bssm.db.bssmgit.global.config.redis.RedisService;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import bssm.db.bssmgit.global.jwt.JwtTokenProvider;
import bssm.db.bssmgit.global.jwt.JwtValidateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public GitIdResponseDto access(String response, RedirectAttributes redirectAttributes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.readValue(response, Map.class);
        String access_token = map.get("access_token");

        URL url = new URL("https://api.github.com/user");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "token " + access_token);

        int responseCode = conn.getResponseCode();
        String result = getResponse(conn, responseCode);

        conn.disconnect();
        redirectAttributes.addFlashAttribute("result", result);

        Map<String, String> gitInfo = objectMapper.readValue(result, Map.class);

        System.out.println("gitInfo.get(\"login\") = " + gitInfo.get("login"));

        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        user.updateGitId(gitInfo.get("login"));

        return GitIdResponseDto.builder()
                .id(gitInfo.get("login"))
                .build();
    }

    @Transactional
    public String getResponse(HttpURLConnection conn, int responseCode) throws IOException {
        StringBuilder sb = new StringBuilder();
        if (responseCode == 200) {
            try (InputStream is = conn.getInputStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    sb.append(line);
                }
            }
        }
        return sb.toString();
    }

}
