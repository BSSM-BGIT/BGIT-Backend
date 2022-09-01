package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.UserService;
import bssm.db.bssmgit.domain.user.web.dto.response.UserResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserApiController {

    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Result findAllUserDesc(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC)
                                      Pageable pageable) {
        List<UserResponseDto> userList = userService.findAll(pageable);
        return new Result(userList.size(), userList);
    }
}
