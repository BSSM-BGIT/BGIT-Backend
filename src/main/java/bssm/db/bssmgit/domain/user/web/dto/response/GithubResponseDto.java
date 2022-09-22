package bssm.db.bssmgit.domain.user.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Data;

@Data
public class GithubResponseDto {

    private final int commits;
    private final String gitId;
    private final String gitMsg;
    private final String img;
    private final UserResponseDto user;

    public GithubResponseDto(User user) {
        this.commits = user.getCommits();
        this.gitId = user.getGithubId();
        this.gitMsg = user.getGithubMsg();
        this.img = user.getImg();
        this.user = new UserResponseDto(user);
    }
}