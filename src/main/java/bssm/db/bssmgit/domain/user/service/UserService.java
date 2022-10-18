package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.domain.user.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.BsmOauthResourceDto;
import bssm.db.bssmgit.domain.user.web.dto.BsmOauthTokenDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserFacade userFacade;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${spring.oauth.bsm.client.id}")
    private String OAUTH_BSM_CLIENT_ID;
    @Value("${spring.oauth.bsm.client.secretKey}")
    private String OAUTH_BSM_CLIENT_SECRET;
    @Value("${spring.oauth.bsm.url.token}")
    private String OAUTH_BSM_TOKEN_URL;
    @Value("${spring.oauth.bsm.url.resource}")
    private String OAUTH_BSM_RESOURCE_URL;

    @Transactional
    public void signup(BsmOauthResourceDto dto, String bsmToken) {
        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .studentGrade(dto.getGrade())
                .studentClassNo(dto.getClassNo())
                .studentNo(dto.getStudentNo())
                .bsmToken(bsmToken)
                // 소마고 4개로 서비스 확장시
                // 리팩토링 필요
                .role(Role.ROLE_BSSM)
                .build();
        userFacade.save(user);
    }

    @Transactional
    public User bsmOauth(String authCode) throws IOException {
        // Payload
        Map<String, String> getTokenPayload = new HashMap<>();
        getTokenPayload.put("clientId", OAUTH_BSM_CLIENT_ID);
        getTokenPayload.put("clientSecret", OAUTH_BSM_CLIENT_SECRET);
        getTokenPayload.put("authcode", authCode);

        // Request
        Request tokenRequest = new Request.Builder()
                .url(OAUTH_BSM_TOKEN_URL)
                .post(RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(getTokenPayload)))
                .build();
        Response tokenResponse = httpClient.newCall(tokenRequest).execute();
        if (tokenResponse.code() == 404) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        BsmOauthTokenDto tokenResponseDto = objectMapper.readValue(Objects.requireNonNull(tokenResponse.body()).string(), BsmOauthTokenDto.class);

        // Payload
        Map<String, String> getResourcePayload = new HashMap<>();
        getResourcePayload.put("clientId", OAUTH_BSM_CLIENT_ID);
        getResourcePayload.put("clientSecret", OAUTH_BSM_CLIENT_SECRET);
        getResourcePayload.put("token", tokenResponseDto.getToken());

        // Request
        Request resourceRequest = new Request.Builder()
                .url(OAUTH_BSM_RESOURCE_URL)
                .post(RequestBody.create(MediaType.parse("application/json"), objectMapper.writeValueAsString(getResourcePayload)))
                .build();
        Response resourceResponse = httpClient.newCall(resourceRequest).execute();

        BsmOauthResourceDto resourceDto = objectMapper.readValue(Objects.requireNonNull(resourceResponse.body()).string(), BsmOauthResourceDto.class);

        // 없는 유저면 회원가입 후 유저 리턴, 이미 있으면 유저를 바로 리턴
        return userFacade.findByEmailOrElseSignUp(resourceDto, tokenResponseDto);
    }

    public UserResponseDto getUser() {
        return new UserResponseDto(userFacade.getCurrentUser());
    }

    public List<GithubResponseDto> findAllUserGitDesc() {
        return userFacade.findAllUserGitDesc();
    }

    public List<BojResponseDto> findAllUserBojDesc() {
        return userFacade.findAllUserBojDesc();
    }
}