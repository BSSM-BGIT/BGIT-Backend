package bssm.db.bssmgit.domain.user.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.Cookie;

@Getter
@Builder
public class CookieResponseDto {

    private Cookie accessToken;
    private Cookie refreshToken;

}
