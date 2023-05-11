package bssm.db.bssmgit.domain.github.service;

import bssm.db.bssmgit.domain.github.domain.repository.CustomGithubRepository;
import bssm.db.bssmgit.domain.github.web.dto.response.GithubResponseDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class GitHubInformationService {

    private final CustomGithubRepository customGithubRepository;

    public List<GithubResponseDto> findAllUserGitDesc() {
        return customGithubRepository.getGitHubAndUser();
    }
}
