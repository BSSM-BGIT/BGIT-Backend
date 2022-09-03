package bssm.db.bssmgit.domain.user.web.api;

import java.io.IOException;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/git")
@RestController
public class GithubApiController {

    private final UserRepository userRepository;

    GitHub github;

    @Value("${spring.oauth.git.url.token}")
    String token;

    @GetMapping("/commits")
    public UserResponseDto gitStatusCurrentUser() throws IOException {
        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        return updateGitCurrentUser(user.getGithubId());
    }


    public UserResponseDto updateGitCurrentUser(String userId) throws IOException {
        try {
            connectToGithub(token);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GIT_CONNECTION_REFUSED);
        }

        System.out.println("getContent : " + github.searchContent().user(userId).list().getTotalCount());

        User user = userRepository.findByEmail(SecurityUtil.getLoginUserEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_LOGIN));

        String gitMsg = github.getUser(userId).getBio();

        user.updateGithubMsg(gitMsg);
        user.updateCommits(github.searchCommits().author(userId).list().getTotalCount());

        return new UserResponseDto(user);
    }

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }

}