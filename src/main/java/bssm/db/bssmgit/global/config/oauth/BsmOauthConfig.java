package bssm.db.bssmgit.global.config.oauth;

import leehj050211.bsmOauth.BsmOauth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BsmOauthConfig {

    @Value("${spring.oauth.bsm.client.id}")
    private String BSM_AUTH_CLIENT_ID;
    @Value("${spring.oauth.bsm.client.secretKey}")
    private String BSM_AUTH_CLIENT_SECRET;

    @Bean("bsmOauth")
    public BsmOauth bsmOauth() {
        return new BsmOauth(BSM_AUTH_CLIENT_ID, BSM_AUTH_CLIENT_SECRET);
    }
}
