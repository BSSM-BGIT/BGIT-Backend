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

    // http://bssm.kro.kr/oauth/login?clientId=4c81669f&redirectURI=http://localhost:8080/auth/oauth/bsm
    @PostMapping("/auth/oauth/bsm")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto loginBsm(HttpServletRequest request) throws IOException {
        return authService.bsmLogin(request.getHeader("authCode"));
    }

    // BSM 인증이 된 상태로 요청해야 GitId가 반환됨
    // https://github.com/login/oauth/authorize?client_id=b87feaccd801817573ad&redirect_uri=http://localhost:8080/auth/github/callback
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