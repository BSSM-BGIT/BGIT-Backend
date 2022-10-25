package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.AuthService;
import bssm.db.bssmgit.domain.user.service.BojService;
import bssm.db.bssmgit.domain.user.web.dto.response.BojAuthenticationResultResDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GitLoginResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.RandomCodeResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.CookieResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;
    private final BojService bojService;

    @PostMapping("/auth/oauth/bsm")
    public CookieResponseDto loginBsm(HttpServletRequest request) throws IOException {
        return authService.bsmLogin(request.getHeader("authCode"));
    }

    @PostMapping("/auth/oauth/github")
    public GitLoginResponseDto loginGit(HttpServletRequest request) throws IOException {
        return authService.gitLogin(request.getHeader("code"));
    }

    @GetMapping("/boj/random")
    public RandomCodeResponseDto getRandomCode(
            HttpServletRequest request)
    {
        return bojService.getRandomCode(request.getHeader("bojId"));
    }

    @PostMapping("/auth/boj")
    public BojAuthenticationResultResDto bojAuthentication() throws IOException {
        return bojService.matchedCode();
    }

    @PostMapping("/refresh")
    public CookieResponseDto getNewAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("REFRESH-TOKEN");
        return authService.getNewAccessToken(refreshToken);
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request.getHeader("ACCESS-TOKEN"));
    }
}