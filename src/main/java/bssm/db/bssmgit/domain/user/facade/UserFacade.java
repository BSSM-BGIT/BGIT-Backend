package bssm.db.bssmgit.domain.user.facade;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.domain.type.Role;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import bssm.db.bssmgit.global.util.SecurityUtil;
import leehj050211.bsmOauth.dto.response.BsmResourceResponse;
import leehj050211.bsmOauth.dto.response.BsmStudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        save(user);
        return user;
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

    public User getAndUpdateOrElseSignUp(BsmResourceResponse resource, String token) {
        Optional<User> user = userRepository.findByEmail(resource.getEmail());
        if (user.isEmpty()) {
            return bsmSignup(resource, token);
        }
        return bsmUserUpdate(user.get(), resource);
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
