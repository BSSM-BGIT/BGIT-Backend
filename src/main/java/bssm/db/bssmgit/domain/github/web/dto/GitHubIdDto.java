package bssm.db.bssmgit.domain.github.web.dto;

import bssm.db.bssmgit.domain.github.domain.GitHub;
import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GitHubIdDto {

    private final String gitHubId;

    @Builder
    public GitHubIdDto(String gitHubId) {
        this.gitHubId = gitHubId;
    }

    public GitHub toGitHubHasThisId() {
        return GitHub.builder()
                .githubId(this.gitHubId)
                .build();
    }
}