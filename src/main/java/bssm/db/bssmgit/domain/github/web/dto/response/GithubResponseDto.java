package bssm.db.bssmgit.domain.github.web.dto.response;

import bssm.db.bssmgit.domain.github.domain.GitHub;
import bssm.db.bssmgit.domain.user.domain.User;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import lombok.Data;

import javax.persistence.Column;

@Data
public class GithubResponseDto {

    private final int commits;

    @Column
    private final String githubId;

    @Column
    private final String githubMsg;

    @Column
    private final String githubImg;

    private final UserResponseDto user;

    public GithubResponseDto(GitHub gitHub, User user) {
        this.commits = gitHub.getCommits();
        this.githubId = gitHub.getGithubId();
        this.githubMsg = gitHub.getGithubMsg();
        this.githubImg = gitHub.getGithubImg();
        this.user = new UserResponseDto(user);
    }
}
