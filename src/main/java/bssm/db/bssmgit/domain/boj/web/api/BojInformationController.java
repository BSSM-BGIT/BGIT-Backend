package bssm.db.bssmgit.domain.boj.web.api;

import bssm.db.bssmgit.domain.boj.service.BojInformationService;
import bssm.db.bssmgit.domain.boj.web.dto.response.BojResponseDto;
import bssm.db.bssmgit.global.generic.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boj")
@RequiredArgsConstructor
public class BojInformationController {
    private final BojInformationService bojInformationService;

    @GetMapping("/ranking")
    public Result<List<BojResponseDto>> findByGithubCommitDesc() {
        List<BojResponseDto> allUserBojDesc = bojInformationService.findAllUserBojDesc();
        return new Result<>(allUserBojDesc.size(), allUserBojDesc);
    }
}
