package bssm.db.bssmgit.domain.github.web.dto.response;

import bssm.db.bssmgit.domain.github.domain.GitHub;
import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Getter;

@Getter
public class GithubOauthResultResDto {

    private boolean result;

    public GithubOauthResultResDto(GitHub gitHub) {
        if (gitHub.getGithubId() != null) result = true;
    }

}
