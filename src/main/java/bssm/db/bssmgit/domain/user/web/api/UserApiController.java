package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.BojService;
import bssm.db.bssmgit.domain.user.service.GithubService;
import bssm.db.bssmgit.domain.user.service.UserService;
import bssm.db.bssmgit.domain.user.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GithubResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserApiController {

    private final UserService userService;
    private final BojService bojService;
    private final GithubService githubService;

    @GetMapping
    public UserResponseDto getUserInfo() {
        return userService.getUser();
    }

    @GetMapping("/git")
    @ResponseStatus(HttpStatus.OK)
    public Result<List<GithubResponseDto>> findByGithubCommitDesc(
            @PageableDefault(size = 10)
            Pageable pageable) {

        List<GithubResponseDto> allUserBojDesc = userService.findAllUserGitDesc(pageable);
        return new Result<>(allUserBojDesc.size(), allUserBojDesc);
    }

    @GetMapping("/boj")
    @ResponseStatus(HttpStatus.OK)
    public Result<List<BojResponseDto>> findByBojTierDesc(
            @PageableDefault(size = 10)
            Pageable pageable) {

        List<BojResponseDto> allUserBojDesc = userService.findAllUserBojDesc(pageable);
        return new Result<>(allUserBojDesc.size(), allUserBojDesc);
    }

    @PostMapping("/test/boj")
    public void bojUpdateTest() {
        bojService.updateUserBojInfo();
    }

    @PostMapping("/test/git")
    public void gitUpdateTest() {
        githubService.updateUserGithub();
    }

}
