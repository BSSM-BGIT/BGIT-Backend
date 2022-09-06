package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.AuthService;
import bssm.db.bssmgit.domain.user.web.dto.response.GitLoginResponseDto;
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

    @PostMapping("/auth/oauth/bsm")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto loginBsm(HttpServletRequest request) throws IOException {
        return authService.bsmLogin(request.getHeader("authCode"));
    }

    @GetMapping("/login/oauth/github")
    public ResponseEntity<GitLoginResponseDto> login(@RequestParam String code) {
        GitLoginResponseDto loginResponse = authService.gitLogin(code);
        return ResponseEntity.ok().body(loginResponse);
    }

    @PutMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto getNewAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("REFRESH-TOKEN");
        return authService.getNewAccessToken(refreshToken);
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request.getHeader("ACCESS-TOKEN"));
    }
}