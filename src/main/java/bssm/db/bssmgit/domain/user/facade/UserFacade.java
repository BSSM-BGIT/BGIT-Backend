package bssm.db.bssmgit.domain.user.facade;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.BsmOauthTokenDto;
import bssm.db.bssmgit.domain.user.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.BsmOauthResourceDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import bssm.db.bssmgit.global.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserRepository userRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void saveAll(List<User> users) {
        userRepository.saveAll(users);
    }

    @Transactional
    public User bsmOauthSignUp(BsmOauthResourceDto dto, String bsmToken) {
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
        save(user);
        return user;
    }

    public User findByEmailOrElseSignUp(BsmOauthResourceDto resourceDto, BsmOauthTokenDto tokenResponseDto) {
        return userRepository.findByEmail(resourceDto.getEmail())
                .orElseGet(() -> bsmOauthSignUp(resourceDto, tokenResponseDto.getToken())
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

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public User getCurrentUser() {
        if (SecurityUtil.getCurrentUser() == null) {
            throw new CustomException(ErrorCode.USER_NOT_LOGIN);
        }
        return SecurityUtil.getCurrentUser().getUser();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findUserImaginaryUser() {
        return userRepository.findByUserImaginaryUser();
    }
}
