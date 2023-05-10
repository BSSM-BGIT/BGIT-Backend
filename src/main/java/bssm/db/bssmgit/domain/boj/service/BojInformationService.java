package bssm.db.bssmgit.domain.boj.service;

import bssm.db.bssmgit.domain.github.domain.repository.GitHubRepository;
import bssm.db.bssmgit.domain.github.web.dto.response.GithubResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BojInformationService {

    private final GitHubRepository gitHubRepository;

    public List<GithubResponseDto> findAllUserGitDesc() {
        return gitHubRepository.findAll().stream()
                .filter(g -> g.getGithubId() != null)
                .filter(g -> g.getCommits() != null)
                .map(GithubResponseDto::new)
                .collect(Collectors.toList());
    }
}
