package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.facade.UserFacade;
import bssm.db.bssmgit.domain.user.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.annotation.ServiceWithTransactionalReadOnly;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.exceptions.BsmAuthCodeNotFoundException;
import leehj050211.bsmOauth.exceptions.BsmAuthInvalidClientException;
import leehj050211.bsmOauth.exceptions.BsmAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@ServiceWithTransactionalReadOnly
public class UserService {

    private final BsmOauth bsmOauth;
    private final UserFacade userFacade;

    @Transactional
    public User bsmOauth(String authCode) throws IOException {
        String token;
        BsmResourceResponse resource;
        try {
            token = bsmOauth.getToken(authCode);
            resource = bsmOauth.getResource(token);
        } catch (BsmAuthCodeNotFoundException | BsmAuthTokenNotFoundException e) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        } catch (BsmAuthInvalidClientException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 없는 유저면 회원가입 후 유저 리턴, 이미 있으면 유저를 바로 리턴
        return userFacade.getAndUpdateOrElseSignUp(resource, token);
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