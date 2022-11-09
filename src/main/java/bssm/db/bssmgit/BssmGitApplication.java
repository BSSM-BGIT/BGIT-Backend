package bssm.db.bssmgit;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@ConfigurationPropertiesScan
@EnableScheduling
@EnableJpaAuditing
@RequiredArgsConstructor
@SpringBootApplication
public class BssmGitApplication {
	public static void main(String[] args) {

		SpringApplication.run(BssmGitApplication.class, args);
	}

}
