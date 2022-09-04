package bssm.db.bssmgit.domain.user.web.api;

import java.io.IOException;

import bssm.db.bssmgit.domain.user.service.GithubService;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RequestMapping("/git")
@RestController
public class GithubApiController {

    private final GithubService githubService;

    @GetMapping("/commits")
    public UserResponseDto gitStatusCurrentUser() throws IOException {
        return githubService.updateGitCurrentUser();
    }

}