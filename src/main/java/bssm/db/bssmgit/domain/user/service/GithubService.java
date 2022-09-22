package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
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
import java.util.ArrayList;

@RequiredArgsConstructor
@Service
public class GithubService {

    private final UserRepository userRepository;

    GitHub github;

    @Value("${spring.oauth.git.url.token}")
    String token;

    @Scheduled(cron = "0 5 * * * *")
    @Transactional
    public void updateGitCurrentUser() {
        try {
            connectToGithub(token);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GIT_CONNECTION_REFUSED);
        }

        ArrayList<User> users = new ArrayList<>();
        userRepository.findAll().stream()
                .filter(u -> u.getGithubId() != null)
                .forEach(u -> {
                    try {
                        int commits = github.searchCommits().author(u.getGithubId())
                                .list().getTotalCount();
                        String bio = github.getUser(u.getGithubId()).getBio();
                        String img = github.getUser(u.getGithubId()).getAvatarUrl();

                        u.updateGitInfo(commits, bio, img);
                        users.add(u);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

        userRepository.saveAll(users);
    }

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }
}
