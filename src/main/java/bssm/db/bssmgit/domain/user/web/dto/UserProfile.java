package bssm.db.bssmgit.domain.user.web.dto;

import bssm.db.bssmgit.domain.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfile {

    private final String gitId;

    @Builder
    public UserProfile(String gitId) {
        this.gitId = gitId;
    }

    public User toUser() {
        return User.builder()
                .githubId(gitId)
                .build();
    }
}