package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
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

    public GithubResponseDto(User user) {
        this.commits = user.getCommits();
        this.githubId = user.getGithubId();
        this.githubMsg = user.getGithubMsg();
        this.githubImg = user.getGithubImg();
        this.user = new UserResponseDto(user);
    }
}
