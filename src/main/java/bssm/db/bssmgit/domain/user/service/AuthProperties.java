package bssm.db.bssmgit.domain.user.service;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "spring.security.oauth2.user.github")
public class AuthProperties {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUrl;

    public AuthProperties(String clientId, String clientSecret, String redirectUrl) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
    }
}
