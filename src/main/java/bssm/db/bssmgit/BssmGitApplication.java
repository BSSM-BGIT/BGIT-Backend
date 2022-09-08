package bssm.db.bssmgit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BssmGitApplication {

	public static void main(String[] args) {
		SpringApplication.run(BssmGitApplication.class, args);
	}

}
