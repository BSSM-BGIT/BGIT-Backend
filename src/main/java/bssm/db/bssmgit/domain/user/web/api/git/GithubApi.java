package bssm.db.bssmgit.domain.user.web.api.git;

import java.io.IOException;

import bssm.db.bssmgit.global.exception.CustomException;
import bssm.db.bssmgit.global.exception.ErrorCode;
import org.kohsuke.github.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/git")
@RestController
public class GithubApi {
    GitHub github;
    String token = "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";

    @GetMapping("/commits")
    public int getCommits(String userId) {
        try {
            connectToGithub(token);
        }
        catch (IOException e) {
            throw new CustomException(ErrorCode.GIT_CONNECTION_REFUSED);
        }

        GHCommitSearchBuilder builder = github.searchCommits()
                .author(userId)
                .sort(GHCommitSearchBuilder.Sort.AUTHOR_DATE);

        PagedSearchIterable<GHCommit> commits = builder.list().withPageSize(100);
        return commits.getTotalCount();
    }

    private void connectToGithub(String token) throws IOException {
        github = new GitHubBuilder().withOAuthToken(token).build();
        github.checkApiUrlValidity();
    }
}
