package bssm.db.bssmgit.domain.github.web.dto.request;

import bssm.db.bssmgit.domain.github.web.dto.GitHubIdDto;

import java.util.Arrays;
import java.util.Map;

public enum OauthAttributes {
    GITHUB("github") {
        @Override
        public GitHubIdDto of(Map<String, Object> attributes) {
            return GitHubIdDto.builder()
                    .gitHubId(String.valueOf(attributes.get("login")))
                    .build();
        }
    };

    private final String providerName;

    OauthAttributes(String name) {
        this.providerName = name;
    }

    public static GitHubIdDto extract(String providerName, Map<String, Object> attributes) {
        return Arrays.stream(values())
                .filter(provider -> providerName.equals(provider.providerName))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .of(attributes);
    }

    public abstract GitHubIdDto of(Map<String, Object> attributes);
}
