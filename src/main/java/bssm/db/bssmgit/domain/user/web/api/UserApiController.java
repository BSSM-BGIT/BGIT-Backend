package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.BojService;
import bssm.db.bssmgit.domain.user.service.UserService;
import bssm.db.bssmgit.domain.user.web.dto.response.BojUserResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserApiController {

    private final UserService userService;
    private final BojService bojService;

    @GetMapping("/git")
    @ResponseStatus(HttpStatus.OK)
    public Result findAllUserGit(
            @PageableDefault(size = 10)
            Pageable pageable) {

        List<UserResponseDto> userList = userService.findAll(pageable);
        return new Result(userList.size(), userList);

    }

    @PostMapping
    public void bojTest() throws IOException {
        bojService.updateUserBojInfo();
    }

    @GetMapping("/boj")
    @ResponseStatus(HttpStatus.OK)
    public Result findAllUserBoj(
            @PageableDefault(size = 10)
            Pageable pageable) {

        List<BojUserResponseDto> allUserBojDesc = bojService.findAllUserBojDesc(pageable);
        return new Result(allUserBojDesc.size(), allUserBojDesc);
    }
}
