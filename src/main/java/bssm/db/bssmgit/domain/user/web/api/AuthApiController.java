package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.AuthService;
import bssm.db.bssmgit.domain.user.service.BojService;
import bssm.db.bssmgit.domain.user.web.dto.response.BojAuthenticationResultResDto;
import bssm.db.bssmgit.domain.user.web.dto.response.GitLoginResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.RandomCodeResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.*;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;
    private final BojService bojService;

    @PostMapping("/auth/oauth/bsm")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto loginBsm(HttpServletRequest request) throws IOException {
        return authService.bsmLogin(request.getHeader("authCode"));
    }

    @PostMapping("/login/oauth/github")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<GitLoginResponseDto> loginGit(@RequestParam String code) throws IOException {
        GitLoginResponseDto loginResponse = authService.gitLogin(code);
        return ResponseEntity.ok().body(loginResponse);
    }

    @GetMapping("/boj/random")
    @ResponseStatus(HttpStatus.OK)
    public RandomCodeResponseDto getRandomCode(@RequestParam String bojId) {
        return bojService.getRandomCode(bojId);
    }

    @PostMapping("/auth/boj")
    @ResponseStatus(HttpStatus.OK)
    public BojAuthenticationResultResDto bojAuthentication() throws IOException {
        return bojService.matchedCode();
    }

    @PutMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto getNewAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("REFRESH-TOKEN");
        return authService.getNewAccessToken(refreshToken);
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(HttpServletRequest request) {
        authService.logout(request.getHeader("ACCESS-TOKEN"));
    }
}