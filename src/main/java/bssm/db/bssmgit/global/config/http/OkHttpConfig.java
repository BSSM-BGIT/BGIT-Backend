package bssm.db.bssmgit.global.config.http;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OkHttpConfig {

    @Bean("okHttpClient")
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}