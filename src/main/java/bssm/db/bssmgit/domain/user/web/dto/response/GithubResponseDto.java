package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Data;

import javax.persistence.Column;

@Data
public class GithubResponseDto {

    private final int commits;

    @Column
    private final String gitId;

    @Column
    private final String gitMsg;

    @Column
    private final String githubImg;

    private final UserResponseDto user;

    public GithubResponseDto(User user) {
        this.commits = user.getCommits();
        this.gitId = user.getGithubId();
        this.gitMsg = user.getGithubMsg();
        this.githubImg = user.getGithubImg();
        this.user = new UserResponseDto(user);
    }
}
