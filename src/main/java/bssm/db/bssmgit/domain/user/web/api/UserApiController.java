package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.GithubService;
import bssm.db.bssmgit.domain.user.service.UserService;
import bssm.db.bssmgit.domain.user.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserApiController {

    private final UserService userService;
    private final GithubService githubService;

    @GetMapping
    public UserResponseDto getUserInfo() {
        return userService.getUser();
    }

    @GetMapping("/git")
    public Result<List<GithubResponseDto>> findByGithubCommitDesc() {
        List<GithubResponseDto> allUserBojDesc = userService.findAllUserGitDesc();
        return new Result<>(allUserBojDesc.size(), allUserBojDesc);
    }

    @GetMapping("/boj")
    public Result<List<BojResponseDto>> findByBojTierDesc() {
        List<BojResponseDto> allUserBojDesc = userService.findAllUserBojDesc();
        return new Result<>(allUserBojDesc.size(), allUserBojDesc);
    }

    @PostMapping("/test")
    public void test() throws IOException {
        githubService.updateUserGithubInfo();
    }

    @DeleteMapping("/test")
    public void deleteTest() throws IOException {
        githubService.deleteNotFoundGithubIdUser();
    }
}
