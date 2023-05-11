package bssm.db.bssmgit.domain.github.web.api;

import bssm.db.bssmgit.domain.boj.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.github.service.GitHubInformationService;
import bssm.db.bssmgit.domain.github.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/github")
@RequiredArgsConstructor
public class GitHubInformationController {

    private final GitHubInformationService gitHubInformationService;

    @GetMapping("/ranking")
    public Result<List<GithubResponseDto>> findByGithubCommitDesc() {
        List<GithubResponseDto> allUserGitDesc = gitHubInformationService.findAllUserGitDesc();
        return new Result<>(allUserGitDesc.size(), allUserGitDesc);
    }
}
