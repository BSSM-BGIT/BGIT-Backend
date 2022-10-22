package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.ImaginaryNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting")
public class ImaginaryNumberApiController {

    private final ImaginaryNumberService imaginaryNumberService;

    @PostMapping("/init") // test
    public void initiate() {
        imaginaryNumberService.init();
    }

    @PutMapping("/designation") // test
    public void designation() {
        imaginaryNumberService.designationImaginaryNumber();
    }

    @PostMapping("/{userId}")
    public void voting(@PathVariable Long userId) {
        imaginaryNumberService.votingImaginaryNumber(userId);
    }
}
