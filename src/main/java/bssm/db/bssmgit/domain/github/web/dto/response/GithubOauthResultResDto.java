package bssm.db.bssmgit.domain.github.web.dto.response;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Getter;

@Getter
public class GithubOauthResultResDto {

    private boolean result;

    public GithubOauthResultResDto(User user) {
        if (user.getGithubId() != null) result = true;
    }

}
