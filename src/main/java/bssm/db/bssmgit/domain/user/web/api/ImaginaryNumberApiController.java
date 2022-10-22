package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.ImaginaryNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting")
public class ImaginaryNumberApiController {

    private final ImaginaryNumberService imaginaryNumberService;

    @PostMapping("/init")
    public void initiate() {
        imaginaryNumberService.init();
    }

    @PostMapping("/{userId}")
    public void voting(@PathVariable Long userId) {
        imaginaryNumberService.votingImaginaryNumber(userId);
    }
}
