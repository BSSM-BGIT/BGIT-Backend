package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.github.service.ImaginaryNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ImaginaryNumberApiController {

    private final ImaginaryNumberService imaginaryNumberService;

    @PostMapping("/{userId}")
    public void report(@PathVariable Long userId) {
        imaginaryNumberService.reportUser(userId);
    }

}
