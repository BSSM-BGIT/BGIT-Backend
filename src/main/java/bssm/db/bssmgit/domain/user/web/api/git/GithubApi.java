package bssm.db.bssmgit.domain.user.web.api.git;

import java.io.IOException;

import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/git")
@RestController
public class GithubApi {
    GitHub github;

    @Value("${spring.oauth.git.url.token}")
    String token;

    @GetMapping("/commits")
    public int getCommits(String userId) {
        try {
            connectToGithub(token);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.GIT_CONNECTION_REFUSED);
        }

        GHCommitSearchBuilder builder = github.searchCommits().author(userId);

        return builder.list().getTotalCount();
    }

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }

}
