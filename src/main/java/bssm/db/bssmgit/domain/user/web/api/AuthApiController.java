package bssm.db.bssmgit.domain.user.web.api;

import bssm.db.bssmgit.domain.user.service.AuthService;
import bssm.db.bssmgit.domain.user.web.dto.GitIdResponseDto;
import bssm.db.bssmgit.domain.user.web.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/auth/oauth/bsm")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponseDto loginBsm(HttpServletRequest request) throws IOException {
        return authService.bsmLogin(request.getHeader("authCode"));
    }

    @GetMapping("/auth/oauth/git")
    public ResponseEntity loginGit(@AuthenticationPrincipal OAuth2User oAuth2User) throws IOException {

        GitHub gitHub = new GitHubBuilder()
                .withOAuthToken(oAuth2User.getName()).build();

        GHUser user = gitHub.getUser(oAuth2User.getName());
        System.out.println("user = " + user);

        GitIdResponseDto dto = GitIdResponseDto.builder()
                .id(user.getLogin())
                .build();

        return ResponseEntity.ok(dto);
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
