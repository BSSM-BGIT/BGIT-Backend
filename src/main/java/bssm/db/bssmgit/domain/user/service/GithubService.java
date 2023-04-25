package bssm.db.bssmgit.domain.user.service;

import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.repository.UserRepository;
import bssm.db.bssmgit.domain.user.util.GithubUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.List;

import static bssm.db.bssmgit.global.exception.ErrorCode.GIT_CONNECTION_REFUSED;
import static bssm.db.bssmgit.global.util.Constants.EVERY_5MINUTES;
import static bssm.db.bssmgit.global.util.Constants.REGEX_FOR_COMMIT;

@RequiredArgsConstructor
@Slf4j
@Service
public class GithubService {

    private final UserRepository userRepository;

    private GitHub github;

    @Value("${spring.oauth.git.url.token}")
    String token;

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }

    @Scheduled(cron = EVERY_5MINUTES)
    public void updateUserGithubInfo() throws IOException {
        connectGithub();
        List<User> users = userRepository.findAll();

        for (int i = 0, userSize = users.size(); i < userSize; i++) {
            User user = users.get(i);
            if (user.hasNotGithubId()) {
                return;
            }
            Integer commit = getCommit(user.getGithubId());
            String bio = github.getUser(user.getGithubId()).getBio();
            String img = github.getUser(user.getGithubId()).getAvatarUrl();

            user.updateGitInfo(commit, bio, img);
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    private Integer getCommit(String githubId) throws IOException {
        URLConnection connection = GithubUtil.getGithubUrlConnection(githubId);
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String commitSymbol = getCommitSymbol(br).replaceAll(",", "");
        return Integer.parseInt(commitSymbol);
    }

    private static String getCommitSymbol(BufferedReader br) throws IOException {
        boolean isMatchRegexForCommit = false;
        String inputLine;

        while ((inputLine = br.readLine()) != null) {
            if (isMatchRegexForCommit) {
                return inputLine.trim();
            }

            if (inputLine.contains(REGEX_FOR_COMMIT)) {
                isMatchRegexForCommit = true;
            }
        }

        throw new IllegalAccessError("커밋 파싱에 실패했습니다.");
    }

    private void connectGithub() {
        try {
            connectToGithub(token);
        } catch (IOException e) {
            throw new CustomException(GIT_CONNECTION_REFUSED);
        }
    }
}
