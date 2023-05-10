package bssm.db.bssmgit.domain.github.domain.repository;

import bssm.db.bssmgit.domain.github.web.dto.response.GithubResponseDto;

import java.util.List;

public interface CustomGithubRepository {
    List<GithubResponseDto> getGitHubAndUser();
}
