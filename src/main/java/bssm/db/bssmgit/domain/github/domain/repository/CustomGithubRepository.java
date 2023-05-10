package bssm.db.bssmgit.domain.github.domain.repository;

import bssm.db.bssmgit.domain.github.domain.GitHub;
import bssm.db.bssmgit.domain.github.web.dto.response.GithubResponseDto;

import java.util.List;

public interface CustomGithubRepository {
    List<GithubResponseDto> getGitHubAndUser();

    List<GitHub> findGitHubsByImaginary();
}
