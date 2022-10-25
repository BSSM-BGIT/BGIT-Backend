package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.ImaginaryNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ImaginaryNumberApiController {

    private final ImaginaryNumberService imaginaryNumberService;

    @PostMapping("/{userId}")
    public void report(@PathVariable Long userId) {
        imaginaryNumberService.reportUser(userId);
    }

    @PutMapping("/update/test") // test
    public void updateTest() {
        imaginaryNumberService.updateImaginaryNumberUser();
    }

    @DeleteMapping("/delete/test") // test
    public void deleteTest() {
        imaginaryNumberService.removeOldReport();
    }

    @PutMapping("/rollback/test")
    public void rollBackTest() {
        imaginaryNumberService.rollbackRealNumber();
    }
}
