package bssm.db.bssmgit.domain.user.web.dto.response;

import lombok.Builder;
import lombok.Getter;

import javax.servlet.http.Cookie;

@Getter
@Builder
public class TokenResponseDto {

    private Cookie accessToken;
    private Cookie refreshToken;

}
