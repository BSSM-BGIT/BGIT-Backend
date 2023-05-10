package bssm.db.bssmgit.domain.github.service;

import bssm.db.bssmgit.domain.github.domain.GitHub;
import bssm.db.bssmgit.domain.github.domain.repository.GitHubRepository;
import bssm.db.bssmgit.domain.github.util.GithubUtil;
import bssm.db.bssmgit.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.List;

import static bssm.db.bssmgit.global.exception.ErrorCode.GIT_CONNECTION_REFUSED;
import static bssm.db.bssmgit.global.util.Constants.EVERY_10MINUTES;
import static bssm.db.bssmgit.global.util.Constants.EVERY_5MINUTES;
import static bssm.db.bssmgit.global.util.Constants.REGEX_FOR_COMMIT;

@RequiredArgsConstructor
@Slf4j
@Service
public class GithubService {

    private final GitHubRepository gitHubRepository;

    private org.kohsuke.github.GitHub github;

    @Value("${spring.oauth.git.url.token}")
    String token;

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }

    @Scheduled(cron = EVERY_10MINUTES)
    @Transactional
    public void updateUserGithubInfo() throws IOException {
        connectGithub();
        List<GitHub> gitHubs = gitHubRepository.findAll();

        for (int i = 0, userSize = gitHubs.size(); i < userSize; i++) {
            GitHub gitHub = gitHubs.get(i);
            if (gitHub.hasNotGithubId()) {
                continue;
            }

            Integer commit = getCommit(gitHub.getGithubId());
            String bio = github.getUser(gitHub.getGithubId()).getBio();
            String img = github.getUser(gitHub.getGithubId()).getAvatarUrl();

            gitHub.updateGitInfo(commit, bio, img);
        }
    }

    private Integer getCommit(String githubId) throws IOException {
        URLConnection connection = GithubUtil.getGithubUrlConnection(githubId);
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String commitSymbol = getCommitSymbol(br).replaceAll(",", "");
        return Integer.parseInt(commitSymbol);
    }

    @Scheduled(cron = EVERY_5MINUTES)
    @Transactional
    public void deleteNotFoundGithubIdUser() throws IOException {
        List<GitHub> gitHubs = gitHubRepository.findAll();
        for (int i = 0, userSize = gitHubs.size(); i < userSize; i++) {
            GitHub gitHub = gitHubs.get(i);
            try {
                URLConnection connection = GithubUtil.getGithubUrlConnection(gitHub.getGithubId());
                connection.getInputStream();
            } catch (FileNotFoundException e) {
                log.info("{}를 찾을 수 없음 -> 해당 유저 깃허브 아이디 자동 삭제 처리", e.getMessage());
                gitHubRepository.delete(gitHub);
            }
        }
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
