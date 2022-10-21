package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.util.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import leehj050211.bsmOauth.BsmOauth;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.dto.response.BsmStudentResponse;
import leehj050211.bsmOauth.exceptions.BsmAuthCodeNotFoundException;
import leehj050211.bsmOauth.exceptions.BsmAuthInvalidClientException;
import leehj050211.bsmOauth.exceptions.BsmAuthTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BsmOauth bsmOauth;

    @Transactional
    private User bsmSignup(BsmResourceResponse dto, String bsmToken) {
        BsmStudentResponse student = dto.getStudent();
        User user = User.builder()
                .email(dto.getEmail())
                .name(student.getName())
                .studentGrade(student.getGrade())
                .studentClassNo(student.getClassNo())
                .studentNo(student.getStudentNo())
                .bsmToken(bsmToken)
                .role(Role.ROLE_BSSM)
                .build();
        return userRepository.save(user);
    }

    @Transactional
    private User bsmUserUpdate(User user, BsmResourceResponse dto) {
        BsmStudentResponse student = dto.getStudent();
        user.updateName(student.getName());
        user.updateStudentGrade(student.getGrade());
        user.updateStudentClassNo(student.getClassNo());
        user.updateStudentNo(student.getStudentNo());
        return userRepository.save(user);
    }

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
        Optional<User> user = userRepository.findByEmail(resource.getEmail());
        if (user.isEmpty()) {
            return bsmSignup(resource, token);
        }
        return bsmUserUpdate(user.get(), resource);
    }

    public UserResponseDto getUser() {
        return new UserResponseDto(
                userRepository.findByEmail(SecurityUtil.getLoginUserEmail()).orElseThrow(
                        () -> new CustomException(ErrorCode.USER_NOT_FOUND)
                )
        );
    }

    public List<GithubResponseDto> findAllUserGitDesc() {
        return userRepository.findGitAll().stream()
                .filter(u -> u.getGithubId() != null)
                .filter(u -> u.getCommits() != null)
                .map(GithubResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<BojResponseDto> findAllUserBojDesc() {
        return userRepository.findBojAll().stream()
                .filter(u -> u.getBojId() != null)
                .map(BojResponseDto::new)
                .collect(Collectors.toList());
    }
}