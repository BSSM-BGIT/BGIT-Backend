package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.config.security.SecurityUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class GithubService {

    private final UserRepository userRepository;

    GitHub github;

    @Value("${spring.oauth.git.url.token}")
    String token;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void updateGitCurrentUser() {
        try {
            connectToGithub(token);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GIT_CONNECTION_REFUSED);
        }

        userRepository.findAll()
                        .forEach(u -> {
                            u.updateCommits(github.searchCommits().author(u.getGithubId())
                                .list().getTotalCount());
                            try {
                                u.updateGithubMsg(github.getUser(u.getGithubId()).getBio());
                                u.updateImg(github.getUser(u.getGithubId()).getAvatarUrl());
                            } catch (IOException e) {
                                throw new CustomException(ErrorCode.GIT_CONNECTION_REFUSED);
                            }
                        });

    }

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }
}
