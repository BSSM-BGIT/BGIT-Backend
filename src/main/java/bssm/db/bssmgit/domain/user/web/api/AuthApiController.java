package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.AuthService;
import bssm.db.bssmgit.domain.user.web.dto.response.GitIdResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.response.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

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

    // https://github.com/login/oauth/authorize?client_id=b87feaccd801817573ad&redirect_uri=http://localhost:8080/auth/github/callback
    @GetMapping("/auth/github/callback")
    public GitIdResponseDto getCode(@RequestParam String code, RedirectAttributes redirectAttributes) throws IOException {
        URL url = new URL("https://github.com/login/oauth/access_token");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
            bw.write("client_id=b87feaccd801817573ad&client_secret=3bb4bd8d01c79d9ef0c7a8402bdc0c3323d77eb8&code=" + code);
            bw.flush();
        }

        int responseCode = conn.getResponseCode();
        String responseData = authService.getResponse(conn, responseCode);

        conn.disconnect();
        return authService.access(responseData, redirectAttributes);
    }

    @DeleteMapping("/logout")
    public void logout(HttpServletRequest request) {
        authService.logout(request.getHeader("ACCESS-TOKEN"));
    }

    @PutMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto getNewAccessToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("REFRESH-TOKEN");
        return authService.getNewAccessToken(refreshToken);
    }


}
